package com.artillexstudios.axapi.database.handler;

import com.artillexstudios.axapi.database.ResultHandler;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class TransformerHandler<T> implements ResultHandler<T> {
    private final Class<T> clazz;

    public TransformerHandler(Class<T> clazz) {
        this.clazz = clazz;
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
            return this.clazz.cast(Arrays.stream(this.clazz.getDeclaredConstructors())
                    .filter(it -> it.getParameterCount() == columnCount)
                    .filter(constructor -> {
                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> clazz = parameterTypes[i];
                            Object object = objects[i];
                            // If we don't know the type, skip it
                            if (object == null) {
                                continue;
                            }

                            if (!clazz.isInstance(object)) {
                                return false;
                            }
                        }

                        return true;
                    })
                    .findFirst()
                    .orElseThrow()
                    .newInstance(objects));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
