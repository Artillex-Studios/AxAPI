package com.artillexstudios.axapi.config.adapters.other;

import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.config.annotation.Ignored;
import com.artillexstudios.axapi.config.annotation.PostProcess;
import com.artillexstudios.axapi.config.annotation.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ObjectAdapter implements TypeAdapter<Object, Map<String, Object>> {
    private final YamlConfiguration.Builder configuration;

    public ObjectAdapter(YamlConfiguration.Builder configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (input.getClass().isAnnotationPresent(Serializable.class)) {
            return input;
        }

        if (input instanceof Map<?, ?> map) {
            Map<String, Object> castMap = (Map<String, Object>) map;
            try {
                Object instance = ((Class<?>) type).getDeclaredConstructor().newInstance();
                for (Field field : instance.getClass().getFields()) {
                    if (Modifier.isFinal(field.getModifiers()) || field.isAnnotationPresent(Ignored.class)) {
                        continue;
                    }

                    Object found = castMap.get(this.configuration.keyRenamer().rename(field.getName()));
                    if (found == null) {
                        castMap.put(this.configuration.keyRenamer().rename(field.getName()), field.get(instance));
                    } else {
                        field.set(instance, holder.deserialize(found, field.getGenericType()));
                    }
                }

                for (Method method : ((Class<?>) type).getMethods()) {
                    if (!method.isAnnotationPresent(PostProcess.class)) {
                        continue;
                    }

                    try {
                        method.invoke(instance);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }

                return instance;
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException();
    }


    @Override
    public Map<String, Object> serialize(TypeAdapterHolder holder, Object value, Type type) {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            for (Field field : value.getClass().getFields()) {
                if (Modifier.isFinal(field.getModifiers()) || field.isAnnotationPresent(Ignored.class)) {
                    continue;
                }

                map.put(this.configuration.keyRenamer().rename(field.getName()), holder.serialize(field.get(value), field.getGenericType()));
            }

            return map;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
