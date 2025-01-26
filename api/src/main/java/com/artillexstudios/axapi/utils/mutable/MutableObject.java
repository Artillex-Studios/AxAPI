package com.artillexstudios.axapi.utils.mutable;

import java.util.Objects;

public final class MutableObject<T> implements Mutable<T> {
    private T value;

    public MutableObject() {

    }

    public MutableObject(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableObject<?> that)) {
            return false;
        }

        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
}
