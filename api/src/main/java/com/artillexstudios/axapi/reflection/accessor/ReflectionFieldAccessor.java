package com.artillexstudios.axapi.reflection.accessor;

import com.artillexstudios.axapi.reflection.FieldAccessor;

import java.lang.reflect.Field;

public class ReflectionFieldAccessor implements FieldAccessor {
    private final Field field;

    public ReflectionFieldAccessor(Field field) {
        this.field = field;
    }

    @Override
    public void set(Object instance, Object argument) {
        try {
            this.field.set(instance, argument);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void setVolatile(Object instance, Object argument) {
        try {
            this.field.set(instance, argument);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Object get(Object instance) {
        try {
            return this.field.get(instance);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Object getVolatile(Object instance) {
        try {
            return this.field.get(instance);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
