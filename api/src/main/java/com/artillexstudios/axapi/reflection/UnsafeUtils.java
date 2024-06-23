package com.artillexstudios.axapi.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public enum UnsafeUtils {
    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(UnsafeUtils.class);
    private Unsafe unsafe;

    UnsafeUtils() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception exception) {
            log.error("An error occurred while initializing FastFieldAccessor!", exception);
        }
    }

    public Unsafe unsafe() {
        return unsafe;
    }
}
