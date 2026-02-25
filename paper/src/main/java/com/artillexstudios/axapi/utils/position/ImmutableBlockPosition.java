package com.artillexstudios.axapi.utils.position;

public record ImmutableBlockPosition(int x, int y, int z) implements BlockPosition {

    public ImmutableBlockPosition(long packedPos) {
        this((int) (packedPos >> 38), (int) (packedPos << 52 >> 52), (int) (packedPos << 26 >> 38));
    }

    public ImmutableBlockPosition() {
        this(0, 0, 0);
    }

    public ImmutableBlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long asLong() {
        return ((long) this.x & 67108863L) << 38 | (long) this.y & 4095L | ((long) this.z & 67108863L) << 12;
    }

    @Override
    public ImmutableBlockPosition immutable() {
        return this;
    }

    @Override
    public MutableBlockPosition mutable() {
        return new MutableBlockPosition(this.x, this.y, this.z);
    }

    public static ImmutableBlockPosition of(long packedPos) {
        return new ImmutableBlockPosition((int) (packedPos >> 38), (int) (packedPos << 52 >> 52), (int) (packedPos << 26 >> 38));
    }
}
