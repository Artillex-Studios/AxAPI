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
        } else if (input instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }

        throw new IllegalArgumentException("Can't convert %s into a BigDecimal!".formatted(input.getClass()));
    }

    @Override
    public String serialize(TypeAdapterHolder holder, BigDecimal value, Type type) {
        return value.toString();
    }
}
