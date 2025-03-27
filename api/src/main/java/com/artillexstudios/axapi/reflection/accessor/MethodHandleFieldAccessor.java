package com.artillexstudios.axapi.reflection.accessor;

import com.artillexstudios.axapi.reflection.FieldAccessor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public class MethodHandleFieldAccessor implements FieldAccessor {
    private final MethodHandle getter;
    private final MethodHandle setter;

    public MethodHandleFieldAccessor(Field field) throws IllegalAccessException {
        this.getter = MethodHandles.privateLookupIn(field.getDeclaringClass(), MethodHandles.lookup()).unreflectGetter(field);
        this.setter = MethodHandles.privateLookupIn(field.getDeclaringClass(), MethodHandles.lookup()).unreflectSetter(field);
    }

    @Override
    public void set(Object instance, Object argument) {
        try {
            this.setter.invoke(instance, argument);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void setVolatile(Object instance, Object argument) {
        try {
            this.setter.invoke(instance, argument);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public Object get(Object instance) {
        try {
            return this.getter.invoke(instance);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public Object getVolatile(Object instance) {
        try {
            return this.getter.invoke(instance);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
