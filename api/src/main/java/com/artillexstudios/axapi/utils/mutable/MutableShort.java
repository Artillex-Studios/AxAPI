package com.artillexstudios.axapi.utils.mutable;

public final class MutableShort implements Mutable<Short> {
    private short value;

    public MutableShort() {

    }

    public MutableShort(Short value) {
        this.value = value;
    }

    public MutableShort(short value) {
        this.value = value;
    }

    @Override
    public Short get() {
        return this.value;
    }

    public void set(short value) {
        this.value = value;
    }

    @Override
    public void set(Short value) {
        this.value = value;
    }

    public short shortValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableShort that)) {
            return false;
        }

        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Short.hashCode(this.value);
    }
}
