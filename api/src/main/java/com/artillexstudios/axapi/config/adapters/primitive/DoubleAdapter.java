package com.artillexstudios.axapi.config.adapters.primitive;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;

import java.lang.reflect.Type;

public final class DoubleAdapter implements TypeAdapter<Double, Double> {

    @Override
    public Double deserialize(TypeAdapterHolder registry, Object input, Type type) {
        if (input instanceof Number num) {
            return num.doubleValue();
        }

        if (input instanceof String str) {
            return Double.parseDouble(str);
        }

        throw new IllegalArgumentException("Can't make a short from a " + input.getClass());
    }

    @Override
    public Double serialize(TypeAdapterHolder registry, Double value, Type type) {
        return value;
    }
}
