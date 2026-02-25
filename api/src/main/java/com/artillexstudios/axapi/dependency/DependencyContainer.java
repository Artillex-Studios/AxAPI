package com.artillexstudios.axapi.dependency;

import java.util.concurrent.ConcurrentHashMap;

public final class DependencyContainer {
    private static final ConcurrentHashMap<Class<?>, Object> container = new ConcurrentHashMap<>();

    public static <T> void register(Class<T> clazz, T instance) {
        if (container.containsKey(clazz)) {
            throw new IllegalStateException("Failed to register dependency as it had already been registered!");
        }

        container.put(clazz, instance);
    }

    public static <T> T getInstance(Class<T> clazz) {
        Object obj = container.get(clazz);
        if (obj == null) {
            throw new IllegalStateException("An instance of this class has not yet been registered!");
        }

        return clazz.cast(obj);
    }
}
