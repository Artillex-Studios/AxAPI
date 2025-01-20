package com.artillexstudios.axapi.utils.mutable;

public final class MutableByte implements Mutable<Byte> {
    private byte value;

    public MutableByte() {

    }

    public MutableByte(Byte value) {
        this.value = value;
    }

    public MutableByte(byte value) {
        this.value = value;
    }

    @Override
    public Byte get() {
        return this.value;
    }

    public void set(byte value) {
        this.value = value;
    }

    @Override
    public void set(Byte value) {
        this.value = value;
    }

    public byte byteValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableByte that)) {
            return false;
        }

        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Byte.hashCode(this.value);
    }
}
