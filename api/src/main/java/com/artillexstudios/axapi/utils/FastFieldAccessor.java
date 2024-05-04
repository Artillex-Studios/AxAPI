package com.artillexstudios.axapi.utils;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FastFieldAccessor {
    private static final Logger log = LoggerFactory.getLogger(FastFieldAccessor.class);
    private static final Unsafe unsafe = UnsafeUtils.INSTANCE.unsafe();
    private final Field field;
    private final long fieldOffset;

    public FastFieldAccessor(Field field) {
        this.field = field;
        this.fieldOffset = unsafe.objectFieldOffset(field);
    }

    public static FastFieldAccessor[] forClass(Class<?> clazz) {
        Preconditions.checkNotNull(clazz, "Can't get FieldAccessor for null class!");
        Field[] declared = clazz.getDeclaredFields();
        FastFieldAccessor[] fieldAccessors = new FastFieldAccessor[declared.length];

        for (int i = 0; i < declared.length; i++) {
            Field field = declared[i];
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);
            fieldAccessors[i] = forField(field);
        }

        return fieldAccessors;
    }

    public static FastFieldAccessor forField(Field field) {
        Preconditions.checkNotNull(field, "Can't get FieldAccessor for null field!");
        return new FastFieldAccessor(field);
    }

    public static FastFieldAccessor forClassField(Class<?> clazz, String field) {
        Preconditions.checkNotNull(clazz, "Can't get FieldAccessor for null class!");
        Preconditions.checkNotNull(field, "Can't get FieldAccessor for null field!");
        Preconditions.checkArgument(!field.isEmpty(), "Can't get FieldAccessor for empty field!");

        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return new FastFieldAccessor(f);
        } catch (NoSuchFieldException e) {
            log.error("An error occurred while creating new FastFieldAccessor for field {} of class {}! Fields of class: {}!", field, clazz.getName(), Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getName() + "-" + f.getType()).collect(Collectors.joining(", ")));
            throw new RuntimeException(e);
        }
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

    public void setBoolean(Object object, boolean value) {
        unsafe.putBoolean(object, fieldOffset, value);
    }

    public void setChar(Object object, char value) {
        unsafe.putChar(object, fieldOffset, value);
    }

    public void setByte(Object object, byte value) {
        unsafe.putByte(object, fieldOffset, value);
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

    public boolean getBoolean(Object object) {
        return unsafe.getBoolean(object, fieldOffset);
    }

    public char getChar(Object object) {
        return unsafe.getChar(object, fieldOffset);
    }

    public byte getByte(Object object) {
        return unsafe.getByte(object, fieldOffset);
    }
    
    public void setAny(Object object, Object value) {
        if (field.getType() == Boolean.TYPE) {
            setBoolean(object, (Boolean) value);
        } else if (field.getType() == Byte.TYPE) {
            setByte(object, (Byte) value);
        } else if (field.getType() == Short.TYPE) {
            setShort(object, (Short) value);
        } else if (field.getType() == Character.TYPE) {
            setChar(object, (Character) value);
        } else if (field.getType() == Integer.TYPE) {
            setInt(object, (Integer) value);
        } else if (field.getType() == Long.TYPE) {
            setLong(object, (Long) value);
        } else if (field.getType() == Float.TYPE) {
            setFloat(object, (Float) value);
        } else if (field.getType() == Double.TYPE) {
            setDouble(object, (Double) value);
        } else {
            set(object, value);
        }
    }

    public Object getAny(Object object) {
        if (field.getType() == Boolean.TYPE) {
            return getBoolean(object);
        } else if (field.getType() == Byte.TYPE) {
            return getByte(object);
        } else if (field.getType() == Short.TYPE) {
            return getShort(object);
        } else if (field.getType() == Character.TYPE) {
            return getChar(object);
        } else if (field.getType() == Integer.TYPE) {
            return getInt(object);
        } else if (field.getType() == Long.TYPE) {
            return getLong(object);
        } else if (field.getType() == Float.TYPE) {
            return getFloat(object);
        } else if (field.getType() == Double.TYPE) {
            return getDouble(object);
        } else {
            return get(object);
        }
    }

    public Field getField() {
        return field;
    }
}