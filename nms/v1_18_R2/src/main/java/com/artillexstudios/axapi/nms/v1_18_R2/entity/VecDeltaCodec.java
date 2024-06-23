package com.artillexstudios.axapi.nms.v1_18_R2.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class VecDeltaCodec {
    private static final double TRUNCATION_STEPS = 4096.0;
    public Vec3 base;

    public VecDeltaCodec() {
        this.base = Vec3.ZERO;
    }

    private static long encode(double value) {
        return Mth.lfloor(value * 4096.0);
    }

    private static double decode(long value) {
        return (double)value / 4096.0;
    }

    public Vec3 decode(long x, long y, long z) {
        if (x == 0L && y == 0L && z == 0L) {
            return this.base;
        } else {
            double d = x == 0L ? this.base.x : decode(encode(this.base.x) + x);
            double e = y == 0L ? this.base.y : decode(encode(this.base.y) + y);
            double f = z == 0L ? this.base.z : decode(encode(this.base.z) + z);
            return new Vec3(d, e, f);
        }
    }

    public long encodeX(Vec3 pos) {
        return encode(pos.x - this.base.x);
    }

    public long encodeY(Vec3 pos) {
        return encode(pos.y - this.base.y);
    }

    public long encodeZ(Vec3 pos) {
        return encode(pos.z - this.base.z);
    }

    public Vec3 delta(Vec3 pos) {
        return pos.subtract(this.base);
    }

    public void setBase(Vec3 pos) {
        this.base = pos;
    }
}
