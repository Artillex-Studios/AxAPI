package com.artillexstudios.axapi.utils.mutable;

public final class MutableLong implements Mutable<Long> {
    private long value;

    public MutableLong() {

    }

    public MutableLong(Long value) {
        this.value = value;
    }

    public MutableLong(long value) {
        this.value = value;
    }

    @Override
    public Long get() {
        return this.value;
    }

    public void set(long value) {
        this.value = value;
    }

    @Override
    public void set(Long value) {
        this.value = value;
    }

    public long longValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableLong that)) {
            return false;
        }

        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }
}
