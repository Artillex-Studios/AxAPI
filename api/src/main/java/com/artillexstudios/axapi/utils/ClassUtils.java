package com.artillexstudios.axapi.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

public enum ClassUtils {
    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(ClassUtils.class);
    private final Unsafe unsafe = UnsafeUtils.INSTANCE.unsafe();

    public boolean classExists(@NotNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Exception exception) {
            return false;
        }
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
