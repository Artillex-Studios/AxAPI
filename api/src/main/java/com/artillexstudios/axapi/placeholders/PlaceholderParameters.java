package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.PlaceholderParameterNotInContextException;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;

import java.util.IdentityHashMap;
import java.util.Map;

public class PlaceholderParameters {
    private static final PlaceholderParameterNotInContextException EXCEPTION = new PlaceholderParameterNotInContextException();
    private final Map<Class<?>, Object> values = new IdentityHashMap<>();

    public <T> PlaceholderParameters withParameter(Class<T> clazz, T value) {
        this.values.put(clazz, value);
        return this;
    }

    public <T> PlaceholderParameters withParameter(T value) {
        for (Class<?> clazz : ClassUtils.INSTANCE.superClasses(value.getClass(), true)) {
            this.values.put(clazz, value);
        }
        return this;
    }

    public PlaceholderParameters withParameters(Object... values) {
        for (Object value : values) {
            this.withParameter(value);
        }
        return this;
    }

    public <T> boolean contains(Class<T> clazz) {
        return this.values.containsKey(clazz);
    }

    public <T> T resolve(Class<T> clazz) throws PlaceholderParameterNotInContextException {
        T object = this.raw(clazz);
        if (object == null) {
            Pair<Class<?>, ThrowingFunction<Object, Object, PlaceholderParameterNotInContextException>> transformer = PlaceholderHandler.transformer(clazz);

            if (transformer == null) {
                throw EXCEPTION;
            }

            Object resolved = this.resolve(transformer.first());
            object = (T) transformer.second().apply(resolved);
        }

        return object;
    }

    public <T> T raw(Class<T> clazz) {
        return (T) this.values.get(clazz);
    }
}
