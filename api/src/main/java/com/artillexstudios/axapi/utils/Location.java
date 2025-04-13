package com.artillexstudios.axapi.utils;

import com.google.common.base.Preconditions;

public class Location {
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public Location(World world, double x, double y, double z) {
        this(world, x, y, z, 0.0f, 0.0f);
    }

    public Location(World world, BlockPosition position) {
        this(world, position, 0.0f, 0.0f);
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = Preconditions.checkNotNull(world, "Can't create a location with an empty world! Use BlockPosition for that!");
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(World world, BlockPosition position, float yaw, float pitch) {
        this.world = Preconditions.checkNotNull(world, "Can't create a location with an empty world! Use BlockPosition for that!");
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    public float yaw() {
        return this.yaw;
    }

    public float pitch() {
        return this.pitch;
    }

    public World world() {
        return this.world;
    }

    public Location x(double x) {
        return new Location(this.world, x, this.y, this.z, this.yaw, this.pitch);
    }

    public Location y(double y) {
        return new Location(this.world, this.x, y, this.z, this.yaw, this.pitch);
    }

    public Location z(double z) {
        return new Location(this.world, this.x, this.y, z, this.yaw, this.pitch);
    }

    public Location yaw(float yaw) {
        return new Location(this.world, this.x, this.y, this.z, yaw, this.pitch);
    }

    public Location pitch(float pitch) {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, pitch);
    }

    public Location add(double x, double y, double z) {
        return new Location(this.world, this.x + x, this.y + y, this.z + z, this.yaw, this.pitch);
    }

    public Location add(double x, double y, double z, float yaw, float pitch) {
        return new Location(this.world, this.x + x, this.y + y, this.z + z, this.yaw + yaw, this.pitch + pitch);
    }

    public Location toCenter() {
        return new Location(this.world, ((int) Math.round(this.x)) + 0.5, (int) Math.round(this.y), ((int) Math.round(this.z)) + 0.5, this.yaw, this.pitch);
    }

    public BlockPosition toBlockPos() {
        return new BlockPosition((int) Math.round(this.x), (int) Math.round(this.y), (int) Math.round(this.z));
    }

    public org.bukkit.Location toBukkit() {
        return new org.bukkit.Location(this.world.toBukkit(), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public Location copy() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
