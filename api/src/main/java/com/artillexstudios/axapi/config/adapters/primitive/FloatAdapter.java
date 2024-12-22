package com.artillexstudios.axapi.config.adapters.primitive;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;

public final class FloatAdapter implements TypeAdapter<Float, Float> {

    @Override
    public Float deserialize(TypeAdapterHolder registry, Object input, Type type) {
        if (input instanceof Number num) {
            return this.validate(num);
        }

        if (input instanceof String str) {
            return this.validate(Double.parseDouble(str));
        }

        throw new IllegalArgumentException("Can't make a float from a " + input.getClass());
    }

    private Float validate(Number number) {
        double doubleValue = number.doubleValue();
        if (doubleValue > (double) Float.MAX_VALUE || doubleValue < (double) Float.MIN_VALUE) {
            throw new IllegalArgumentException("Float value is not actually a float! Should be between %s and %s, but was %s".formatted(Byte.MIN_VALUE, Byte.MAX_VALUE, doubleValue));
        }

        return (float) doubleValue;
    }

    @Override
    public Float serialize(TypeAdapterHolder registry, Float value, Type type) {
        return value;
    }
}
