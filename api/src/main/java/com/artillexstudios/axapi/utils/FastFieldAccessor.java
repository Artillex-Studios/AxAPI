package com.artillexstudios.axapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class FastFieldAccessor {
    private static final Logger log = LoggerFactory.getLogger(FastFieldAccessor.class);
    private static Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception exception) {
            log.error("An error occurred while initializing FastFieldAccessor!", exception);
        }
    }

    private final Field field;
    private final long fieldOffset;

    public FastFieldAccessor(Field field) {
        this.field = field;
        this.fieldOffset = unsafe.objectFieldOffset(field);
    }

    public <T> void set(Object object, T value) {
        unsafe.putObject(object, fieldOffset, value);
    }

    public void setInt(Object object, int value) {
        unsafe.putInt(object, fieldOffset, value);
    }

    public void setLong(Object object, long value) {
        unsafe.putLong(object, fieldOffset, value);
    }

    public void setShort(Object object, short value) {
        unsafe.putShort(object, fieldOffset, value);
    }

    public void setDouble(Object object, double value) {
        unsafe.putDouble(object, fieldOffset, value);
    }

    public void setFloat(Object object, float value) {
        unsafe.putFloat(object, fieldOffset, value);
    }

    public <T> T get(Object object) {
        return (T) unsafe.getObject(object, fieldOffset);
    }

    public int getInt(Object object) {
        return unsafe.getInt(object, fieldOffset);
    }

    public short getShort(Object object) {
        return unsafe.getShort(object, fieldOffset);
    }

    public long getLong(Object object) {
        return unsafe.getLong(object, fieldOffset);
    }

    public double getDouble(Object object) {
        return unsafe.getDouble(object, fieldOffset);
    }

    public float getFloat(Object object) {
        return unsafe.getFloat(object, fieldOffset);
    }
}