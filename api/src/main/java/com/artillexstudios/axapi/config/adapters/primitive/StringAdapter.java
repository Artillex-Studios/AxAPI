package com.artillexstudios.axapi.config.adapters.primitive;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;

public final class StringAdapter implements TypeAdapter<String, String> {

    @Override
    public String deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (input instanceof String str) {
            return str;
        }

        return input.toString();
    }

    @Override
    public String serialize(TypeAdapterHolder holder, String value, Type type) {
        return value;
    }
}
