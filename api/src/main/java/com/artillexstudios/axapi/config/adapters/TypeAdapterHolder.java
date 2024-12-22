package com.artillexstudios.axapi.config.adapters;

import com.artillexstudios.axapi.config.adapters.collections.LinkedHashMapAdapter;
import com.artillexstudios.axapi.config.adapters.collections.ListAdapter;
import com.artillexstudios.axapi.config.adapters.collections.MapAdapter;
import com.artillexstudios.axapi.config.adapters.other.EnumAdapter;
import com.artillexstudios.axapi.config.adapters.primitive.BooleanAdapter;
import com.artillexstudios.axapi.config.adapters.primitive.ByteAdapter;
import com.artillexstudios.axapi.config.adapters.primitive.DoubleAdapter;
import com.artillexstudios.axapi.config.adapters.primitive.FloatAdapter;
import com.artillexstudios.axapi.config.adapters.primitive.IntegerAdapter;
import com.artillexstudios.axapi.config.adapters.primitive.LongAdapter;
import com.artillexstudios.axapi.config.adapters.primitive.ShortAdapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TypeAdapterHolder {
    private final Map<Class<?>, TypeAdapter<?, ?>> adapters = new HashMap<>();

    public TypeAdapterHolder() {
        this.adapters.put(boolean.class, new BooleanAdapter());
        this.adapters.put(byte.class, new ByteAdapter());
        this.adapters.put(double.class, new DoubleAdapter());
        this.adapters.put(float.class, new FloatAdapter());
        this.adapters.put(int.class, new IntegerAdapter());
        this.adapters.put(long.class, new LongAdapter());
        this.adapters.put(short.class, new ShortAdapter());

        this.adapters.put(Boolean.class, new BooleanAdapter());
        this.adapters.put(Byte.class, new ByteAdapter());
        this.adapters.put(Double.class, new DoubleAdapter());
        this.adapters.put(Float.class, new FloatAdapter());
        this.adapters.put(Integer.class, new IntegerAdapter());
        this.adapters.put(Long.class, new LongAdapter());
        this.adapters.put(Short.class, new ShortAdapter());
        this.adapters.put(String.class, new ShortAdapter());
        this.adapters.put(Enum.class, new EnumAdapter());

        this.adapters.put(List.class, new ListAdapter());
        this.adapters.put(Map.class, new MapAdapter());
        this.adapters.put(LinkedHashMap.class, new LinkedHashMapAdapter());
    }

    public void registerAdapter(Class<?> clazz, TypeAdapter<?, ?> adapter) {
        this.adapters.put(clazz, adapter);
    }

    public void registerAdapters(Map<Class<?>, TypeAdapter<?, ?>> adapters) {
        this.adapters.putAll(adapters);
    }

    // To yaml
    public Object serialize(Object object, Type type) {
        TypeAdapter<Object, Object> adapter = null;
        if (type instanceof Class<?> clazz) {
            if (Enum.class.isAssignableFrom(clazz)) {
                adapter = (TypeAdapter<Object, Object>) this.adapters.get(Enum.class);
            } else {
                adapter = (TypeAdapter<Object, Object>) this.adapters.get(clazz);
            }
        }

        if (adapter == null && type instanceof ParameterizedType parameterizedType) {
            if (Enum.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                adapter = (TypeAdapter<Object, Object>) this.adapters.get(Enum.class);
            } else {
                adapter = (TypeAdapter<Object, Object>) this.adapters.get((Class<?>) parameterizedType.getRawType());
            }
        }

        if (adapter == null) {
            adapter = (TypeAdapter<Object, Object>) this.adapters.get(object.getClass());
        }

        if (adapter == null) {
            throw new IllegalArgumentException();
        }

        return adapter.serialize(this, object, type);
    }

    // From the yaml
    public Object deserialize(Object object, Type type) {
        TypeAdapter<?, ?> adapter = null;
        if (type instanceof Class<?> clazz) {
            if (Enum.class.isAssignableFrom(clazz)) {
                adapter = this.adapters.get(Enum.class);
            } else {
                adapter = this.adapters.get(clazz);
            }
        }

        if (adapter == null && type instanceof ParameterizedType parameterizedType) {
            if (Enum.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                adapter = this.adapters.get(Enum.class);
            } else {
                adapter = this.adapters.get((Class<?>) parameterizedType.getRawType());
            }
        }

        if (adapter == null) {
            adapter = this.adapters.get(object.getClass());
        }

        if (adapter == null) {
            throw new IllegalArgumentException();
        }

        return adapter.deserialize(this, object, type);
    }
}