package com.artillexstudios.axapi.utils.mutable;

public final class MutableBoolean implements Mutable<Boolean> {
    private boolean value;

    public MutableBoolean() {

    }

    public MutableBoolean(Boolean value) {
        this.value = value;
    }

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean get() {
        return this.value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    public boolean booleanValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableBoolean that)) {
            return false;
        }

        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(this.value);
    }
}
