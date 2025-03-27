package com.artillexstudios.axapi.reflection.accessor;

import com.artillexstudios.axapi.reflection.FieldAccessor;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

public class VarHandleFieldAccessor implements FieldAccessor {
    private final VarHandle handle;

    public VarHandleFieldAccessor(Field field) throws IllegalAccessException {
        this.handle = MethodHandles.privateLookupIn(field.getDeclaringClass(), MethodHandles.lookup()).unreflectVarHandle(field);
    }

    @Override
    public void set(Object instance, Object argument) {
        this.handle.set(instance, argument);
    }

    @Override
    public void setVolatile(Object instance, Object argument) {
        this.handle.setVolatile(instance, argument);
    }

    @Override
    public Object get(Object instance) {
        return this.handle.get(instance);
    }

    @Override
    public Object getVolatile(Object instance) {
        return this.handle.getVolatile(instance);
    }
}
