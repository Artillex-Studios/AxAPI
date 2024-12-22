package com.artillexstudios.axapi.config.adapters.collections;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LinkedHashMapAdapter implements TypeAdapter<LinkedHashMap<String, Object>, LinkedHashMap<String, Object>> {

    @Override
    public LinkedHashMap<String, Object> deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new RuntimeException();
        }

        Type t = parameterizedType.getActualTypeArguments()[1];
        if (input instanceof LinkedHashMap<?, ?> map) {
            LinkedHashMap<String, Object> returning = new LinkedHashMap<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                returning.put((String) entry.getKey(), holder.deserialize(entry.getValue(), t));
            }

            return returning;
        }

        throw new RuntimeException();
    }

    @Override
    public LinkedHashMap<String, Object> serialize(TypeAdapterHolder holder, LinkedHashMap<String, Object> value, Type type) {
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new RuntimeException();
        }

        Type t = parameterizedType.getActualTypeArguments()[1];
        LinkedHashMap<String, Object> returning = new LinkedHashMap<>();

        for (Map.Entry<?, ?> entry : value.entrySet()) {
            returning.put((String) entry.getKey(), holder.serialize(entry.getValue(), t));
        }

        return returning;
    }
}
