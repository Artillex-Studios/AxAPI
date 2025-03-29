package com.artillexstudios.shared.axapi.utils;

public class BlockPosition {
    protected int x;
    protected int y;
    protected int z;

    public BlockPosition() {
        this(0, 0, 0);
    }

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long asLong() {
        return ((long) this.x & 67108863L) << 38 | (long) this.y & 4095L | ((long) this.z & 67108863L) << 12;
    }

    public static BlockPosition of(long packedPos) {
        return new BlockPosition((int) (packedPos >> 38), (int) (packedPos << 52 >> 52), (int) (packedPos << 26 >> 38));
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof BlockPosition that)) return false;

        return this.x == that.x && this.y == that.y && this.z == that.z;
    }

    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }

    public static class Mutable extends BlockPosition {

        public Mutable() {
            this(0, 0, 0);
        }

        public Mutable(int x, int y, int z) {
            super(x, y, z);
        }

        public void add(int x, int y, int z) {
            this.x += x;
            this.y += y;
            this.z += z;
        }

        public int x() {
            return this.x;
        }

        public void x(int x) {
            this.x = x;
        }

        public int y() {
            return this.y;
        }

        public void y(int y) {
            this.y = y;
        }

        public int z() {
            return this.z;
        }

        public void z(int z) {
            this.z = z;
        }
    }
}
