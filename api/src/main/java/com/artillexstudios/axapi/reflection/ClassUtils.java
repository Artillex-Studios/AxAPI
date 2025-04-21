package com.artillexstudios.axapi.reflection;

import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public enum ClassUtils {
    INSTANCE;

    private final LoadingCache<String, Class<?>> CLASS_CACHE = Caffeine.newBuilder()
            .maximumSize(50)
            .build(name -> {
                try {
                    return Class.forName(name, false, this.getClass().getClassLoader());
                } catch (ClassNotFoundException exception) {
                    return null;
                }
            });

    public boolean classExists(@NotNull String className) {
        return CLASS_CACHE.get(className) != null;
    }

    public <T> T newInstance(String clazz) {
        try {
            return newInstance(Class.forName(clazz));
        } catch (ClassNotFoundException exception) {
            LogUtils.error("Could not find class {}!", clazz, exception);
            throw new RuntimeException(exception);
        }
    }

    public Class<?> getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException exception) {
            LogUtils.error("An unexpected error occurred while finding class {}!", clazz, exception);
            throw new RuntimeException(exception);
        }
    }

    public boolean isClass(Class<?> clazz, String other) {
        return CLASS_CACHE.get(other) == clazz;
    }

    public Field getDeclaredField(String clazz, String field) {
        Class<?> cl = getClass(clazz);
        try {
            return cl.getDeclaredField(field);
        } catch (NoSuchFieldException exception) {
            LogUtils.error("An unexpected error occurred while getting field {} of class {}!", field, clazz, exception);
            throw new RuntimeException(exception);
        }
    }

    public <T> T newInstance(Class<?> clazz) {
        return null;
    }
}
