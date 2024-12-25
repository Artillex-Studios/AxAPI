package com.artillexstudios.axapi.config.adapters.other;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.UUID;

public final class BigIntegerAdapter implements TypeAdapter<BigInteger, String> {

    @Override
    public BigInteger deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (input instanceof String str) {
            return new BigInteger(str);
        }

        return null;
    }

    @Override
    public String serialize(TypeAdapterHolder holder, BigInteger value, Type type) {
        return value.toString();
    }
}
