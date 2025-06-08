package com.artillexstudios.axapi.config.adapters.other;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;
import java.util.UUID;

public final class UUIDAdapter implements TypeAdapter<UUID, String> {

    @Override
    public UUID deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (input instanceof String str) {
            return UUID.fromString(str);
        } else if (input instanceof UUID uuid){
            return uuid;
        }

        throw new IllegalArgumentException("Can't convert %s into a UUID!".formatted(input.getClass()));
    }

    @Override
    public String serialize(TypeAdapterHolder holder, UUID value, Type type) {
        return value.toString();
    }
}
