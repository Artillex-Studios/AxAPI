package com.artillexstudios.axapi.database.handler;

import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.database.ResultHandler;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransformerHandler<T> implements ResultHandler<T> {
    private final TypeAdapterHolder holder;
    private final Class<T> clazz;

    public TransformerHandler(Class<T> clazz) {
        this(clazz, new TypeAdapterHolder());
    }

    public TransformerHandler(Class<T> clazz, TypeAdapterHolder typeAdapterHolder) {
        this.clazz = clazz;
        this.holder = typeAdapterHolder;
    }

    @Override
    public T handle(ResultSet resultSet, boolean checkNext) throws SQLException {
        if (checkNext && !resultSet.next()) {
            return null;
        }

        int columnCount = resultSet.getMetaData().getColumnCount();
        Object[] objects = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            objects[i] = resultSet.getObject(i + 1);
        }

        try {
            Object[] deserialized = new Object[columnCount];
            outer:
            for (Constructor<?> declaredConstructor : this.clazz.getDeclaredConstructors()) {
                if (declaredConstructor.getParameterCount() != columnCount) {
                    continue;
                }

                Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    Object object = objects[i];

                    if (object != null && parameterType.isAssignableFrom(object.getClass())) {
                        deserialized[i] = object;
                        continue;
                    }

                    try {
                        deserialized[i] = this.holder.deserialize(object, parameterType);
                    } catch (Exception exception) {
                        if (FeatureFlags.DEBUG.get()) {
                            LogUtils.info("Failed to convert {} into {}!", object, parameterType);
                        }
                        continue outer;
                    }
                }

                return UncheckedUtils.unsafeCast(declaredConstructor.newInstance(deserialized));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
