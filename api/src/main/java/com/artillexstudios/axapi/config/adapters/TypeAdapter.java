package com.artillexstudios.axapi.config.adapters;

import java.lang.reflect.Type;

public interface TypeAdapter<T, Z> {

    T deserialize(TypeAdapterHolder holder, Object input, Type type);

    Z serialize(TypeAdapterHolder holder, T value, Type type);
}
