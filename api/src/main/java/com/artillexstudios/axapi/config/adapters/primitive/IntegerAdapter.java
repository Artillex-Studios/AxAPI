package com.artillexstudios.axapi.config.adapters.primitive;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;
import java.math.BigInteger;

public final class IntegerAdapter implements TypeAdapter<Integer, Integer> {

    @Override
    public Integer deserialize(TypeAdapterHolder registry, Object input, Type type) {
        if (input instanceof Number num) {
            return this.validate(num);
        }

        if (input instanceof String str) {
            return this.validate(Double.parseDouble(str));
        }

        throw new IllegalArgumentException("Can't make an int from a " + input.getClass());
    }

    private Integer validate(Number number) {
        long longValue = number instanceof BigInteger bigInteger ? bigInteger.longValueExact() : number.longValue();
        if (longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Int value is not actually an int! Should be between %s and %s, but was %s".formatted(Integer.MIN_VALUE, Integer.MAX_VALUE, longValue));
        }

        return (int) longValue;
    }

    @Override
    public Integer serialize(TypeAdapterHolder registry, Integer value, Type type) {
        return value;
    }
}
