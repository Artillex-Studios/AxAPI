package com.artillexstudios.axapi.context;

import com.artillexstudios.axapi.utils.UncheckedUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HashMapContext {
    private final HashMap<ContextKey<?>, Object> values;

    public HashMapContext() {
        this(new HashMap<>());
    }

    public HashMapContext(HashMap<ContextKey<?>, Object> contents) {
        this.values = contents;
    }

    public static HashMapContext create() {
        return new HashMapContext();
    }

    public <T> HashMapContext with(ContextKey<T> key, T value) {
        this.values.put(key, value);
        return this;
    }

    public <T> T getByClass(Class<T> clazz) {
        for (Map.Entry<ContextKey<?>, Object> entry : this.values.entrySet()) {
            if (entry.getKey().type().equals(clazz)) {
                return clazz.cast(entry.getValue());
            }
        }

        return null;
    }

    public <T> T getByName(String name) {
        for (Map.Entry<ContextKey<?>, Object> entry : this.values.entrySet()) {
            if (entry.getKey().name().equals(name)) {
                return UncheckedUtils.unsafeCast(entry.getValue());
            }
        }

        return null;
    }

    public <T> T get(ContextKey<T> key) {
        return key.type().cast(this.values.get(key));
    }

    public HashMapContext copy() {
        return new HashMapContext(UncheckedUtils.unsafeCast(this.values.clone()));
    }

    /**
     * Merges the values of the other context into the values of this.
     * This won't replace anything from this context.
     * @param other The other context.
     */
    public HashMapContext merge(HashMapContext other) {
        for (Map.Entry<ContextKey<?>, Object> entry : other.values.entrySet()) {
            if (this.values.containsKey(entry.getKey())) {
                continue;
            }

            this.values.put(entry.getKey(), entry.getValue());
        }

        return this;
    }

    @Override
    public final boolean equals(Object object) {
        if (!(object instanceof HashMapContext context)) {
            return false;
        }

        return Objects.equals(this.values, context.values);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.values);
    }
}
