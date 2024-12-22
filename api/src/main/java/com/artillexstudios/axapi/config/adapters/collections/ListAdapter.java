package com.artillexstudios.axapi.config.adapters.collections;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ListAdapter implements TypeAdapter<List<Object>, List<Object>> {

    @Override
    public List<Object> deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new RuntimeException();
        }

        Type t = parameterizedType.getActualTypeArguments()[0];
        if (input instanceof List<?> list) {
            List<Object> returning = new ArrayList<>(list.size());

            for (Object o : list) {
                returning.add(holder.deserialize(o, t));
            }

            return returning;
        }

        throw new RuntimeException();
    }

    @Override
    public List<Object> serialize(TypeAdapterHolder holder, List<Object> value, Type type) {
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new RuntimeException();
        }

        Type t = parameterizedType.getActualTypeArguments()[0];
        List<Object> returning = new ArrayList<>(value.size());

        for (Object o : value) {
            returning.add(holder.serialize(o, t));
        }

        return returning;
    }
}
