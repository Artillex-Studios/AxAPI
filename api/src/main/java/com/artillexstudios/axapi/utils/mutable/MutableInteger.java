package com.artillexstudios.axapi.utils.mutable;

public final class MutableInteger implements Mutable<Integer> {
    private int value;

    public MutableInteger() {

    }

    public MutableInteger(Integer value) {
        this.value = value;
    }

    public MutableInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    public void set(int value) {
        this.value = value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }

    public int intValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableInteger that)) {
            return false;
        }

        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.value);
    }
}
