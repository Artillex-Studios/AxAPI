package com.artillexstudios.axapi.config.adapters.other;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class BigDecimalAdapter implements TypeAdapter<BigDecimal, String> {

    @Override
    public BigDecimal deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (input instanceof String str) {
            return new BigDecimal(str);
        }

        return null;
    }

    @Override
    public String serialize(TypeAdapterHolder holder, BigDecimal value, Type type) {
        return value.toString();
    }
}
