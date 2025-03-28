package com.artillexstudios.axapi.reflection;

import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public enum ClassUtils {
    INSTANCE;

    private final Cache<String, Boolean> CLASS_CACHE = Caffeine.newBuilder()
            .maximumSize(50)
            .build();

    public boolean classExists(@NotNull String className) {
        return Boolean.TRUE.equals(CLASS_CACHE.get(className, name -> {
            try {
                Class.forName(name, false, this.getClass().getClassLoader());
                return true;
            } catch (ClassNotFoundException exception) {
                return false;
            }
        }));
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
