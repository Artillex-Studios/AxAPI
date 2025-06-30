package com.artillexstudios.axapi.context;

import java.util.HashMap;
import java.util.Map;

public class HashMapContext {
    private final HashMap<ContextKey<?>, Object> values = new HashMap<>();

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
                return (T) entry.getValue();
            }
        }

        return null;
    }

    public <T> T get(ContextKey<T> key) {
        return key.type().cast(this.values.get(key));
    }
}
