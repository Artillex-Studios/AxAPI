package com.artillexstudios.axapi.config.adapters;

import java.lang.reflect.Type;
import java.util.List;

public interface TypeAdapter<T, Z> {

    T deserialize(TypeAdapterHolder holder, Object input, Type type);

    Z serialize(TypeAdapterHolder holder, T value, Type type);

    default List<String> values(T input) {
        return List.of();
    }
}
