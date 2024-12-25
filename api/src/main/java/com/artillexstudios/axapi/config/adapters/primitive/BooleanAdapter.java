package com.artillexstudios.axapi.config.adapters.primitive;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;

public final class BooleanAdapter implements TypeAdapter<Boolean, Boolean> {

    @Override
    public Boolean deserialize(TypeAdapterHolder registry, Object input, Type type) {
        if (input instanceof Boolean bool) {
            return bool;
        }

        if (input instanceof String str) {
            if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("on") || str.equalsIgnoreCase("enabled")) {
                return Boolean.TRUE;
            }

            if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("off") || str.equalsIgnoreCase("disabled")) {
                return Boolean.FALSE;
            }

            throw new IllegalArgumentException("Not a boolean type: " + str);
        }

        throw new IllegalArgumentException("Not a boolean type: " + input.getClass());
    }

    @Override
    public Boolean serialize(TypeAdapterHolder registry, Boolean value, Type type) {
        return value;
    }
}
