package com.artillexstudios.axapi.config.adapters.collections;

import com.artillexstudios.axapi.config.adapters.MapConfigurationGetter;
import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.utils.UncheckedUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class MapConfigurationGetterAdapter implements TypeAdapter<MapConfigurationGetter, Map<String, Object>> {

    @Override
    public MapConfigurationGetter deserialize(TypeAdapterHolder holder, Object input, Type type) {
        Type t = null;
        if (type instanceof ParameterizedType parameterizedType) {
            t = parameterizedType.getActualTypeArguments()[1];
        }

        if (input instanceof Map<?, ?> map) {
            HashMap<String, Object> returning = new HashMap<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                returning.put((String) entry.getKey(), holder.deserialize(entry.getValue(), t));
            }

            return new MapConfigurationGetter(UncheckedUtils.unsafeCast(returning));
        }

        if (input instanceof MapConfigurationGetter getter) {
            return getter;
        }

        throw new RuntimeException();
    }

    @Override
    public Map<String, Object> serialize(TypeAdapterHolder holder, MapConfigurationGetter value, Type type) {
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new RuntimeException();
        }

        Type t = parameterizedType.getActualTypeArguments()[1];
        HashMap<String, Object> returning = new HashMap<>();

        for (Map.Entry<?, ?> entry : value.wrapped().entrySet()) {
            returning.put((String) entry.getKey(), holder.serialize(entry.getValue(), t));
        }

        return returning;
    }
}
