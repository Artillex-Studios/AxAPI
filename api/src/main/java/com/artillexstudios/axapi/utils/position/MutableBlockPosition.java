package com.artillexstudios.axapi.utils.position;

public final class MutableBlockPosition implements BlockPosition {
    private int x;
    private int y;
    private int z;

    public MutableBlockPosition() {
        this(0, 0, 0);
    }

    public MutableBlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int x() {
        return this.x;
    }

    public void x(int x) {
        this.x = x;
    }

    @Override
    public int y() {
        return this.y;
    }

    public void y(int y) {
        this.y = y;
    }

    @Override
    public int z() {
        return this.z;
    }

    public void z(int z) {
        this.z = z;
    }

    @Override
    public long asLong() {
        return ((long) this.x & 67108863L) << 38 | (long) this.y & 4095L | ((long) this.z & 67108863L) << 12;
    }

    @Override
    public ImmutableBlockPosition immutable() {
        return new ImmutableBlockPosition(this.x, this.y, this.z);
    }

    @Override
    public MutableBlockPosition mutable() {
        return this;
    }
}
