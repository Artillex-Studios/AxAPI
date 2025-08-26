package com.artillexstudios.axapi.config.adapters.collections;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class MapAdapter implements TypeAdapter<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> deserialize(TypeAdapterHolder holder, Object input, Type type) {
        Type t = null;
        if (type instanceof ParameterizedType parameterizedType) {
            t = parameterizedType.getActualTypeArguments()[1];
        }

        if (input instanceof Map<?, ?> map) {
            HashMap<String, Object> returning = new HashMap<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                returning.put((String) entry.getKey(), holder.deserialize(entry.getValue(), t));
            }

            return returning;
        }

        throw new RuntimeException();
    }

    @Override
    public Map<String, Object> serialize(TypeAdapterHolder holder, Map<String, Object> value, Type type) {
        HashMap<String, Object> returning = new HashMap<>();

        Type t = null;
        boolean hasParameterizedType = false;
        if (type instanceof ParameterizedType parameterizedType) {
            t = parameterizedType.getActualTypeArguments()[1];
            hasParameterizedType = true;
        }

        for (Map.Entry<?, ?> entry : value.entrySet()) {
            if (!hasParameterizedType) {
                t = entry.getValue().getClass();
            }

            returning.put((String) entry.getKey(), holder.serialize(entry.getValue(), t));
        }

        return returning;
    }
}