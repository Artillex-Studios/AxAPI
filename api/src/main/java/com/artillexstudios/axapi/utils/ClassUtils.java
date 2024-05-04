package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.collections.ThreadSafeList;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.util.HashMap;

public enum ClassUtils {
    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(ClassUtils.class);
    private final Unsafe unsafe = UnsafeUtils.INSTANCE.unsafe();
    private final HashMap<String, Boolean> CLASS_CACHE = new HashMap<>();

    public boolean classExists(@NotNull String className) {
        return CLASS_CACHE.computeIfAbsent(className, name -> {
            try {
                Class.forName(name, false, this.getClass().getClassLoader());
                return true;
            } catch (Exception exception) {
                return false;
            }
        });
    }

    public <T> T newInstance(String clazz) {
        try {
            return newInstance(Class.forName(clazz));
        } catch (ClassNotFoundException exception) {
            log.error("Could not find class {}!", clazz, exception);
            throw new RuntimeException(exception);
        }
    }

    public <T> T newInstance(Class<?> clazz) {
        try {
            return (T) unsafe.allocateInstance(clazz);
        } catch (InstantiationException exception) {
            log.error("Failed to initialize new instance of class {}!", clazz.getName(), exception);
            throw new RuntimeException(exception);
        }
    }
}
