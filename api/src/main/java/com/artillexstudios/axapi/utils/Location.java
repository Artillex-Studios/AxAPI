package com.artillexstudios.axapi.utils;

import com.google.common.base.Preconditions;

import java.util.Objects;

public final class Location {
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public static Location create(org.bukkit.Location location) {
        return create(World.create(location.getWorld()), location);
    }

    public static Location create(World world, org.bukkit.Location location) {
        return new Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

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

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public World getWorld() {
        return this.world;
    }

    public double getBlockX() {
        return Math.floor(this.x);
    }

    public double getBlockY() {
        return Math.floor(this.y);
    }

    public double getBlockZ() {
        return Math.floor(this.z);
    }

    public Location withX(double x) {
        return new Location(this.world, x, this.y, this.z, this.yaw, this.pitch);
    }

    public Location withY(double y) {
        return new Location(this.world, this.x, y, this.z, this.yaw, this.pitch);
    }

    public Location withZ(double z) {
        return new Location(this.world, this.x, this.y, z, this.yaw, this.pitch);
    }

    public Location withYaw(float yaw) {
        return new Location(this.world, this.x, this.y, this.z, yaw, this.pitch);
    }

    public Location withPitch(float pitch) {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, pitch);
    }

    public Location addX(double x) {
        return new Location(this.world, this.x + x, this.y, this.z, this.yaw, this.pitch);
    }

    public Location addY(double y) {
        return new Location(this.world, this.x, this.y + y, this.z, this.yaw, this.pitch);
    }

    public Location addZ(double z) {
        return new Location(this.world, this.x, this.y, this.z + z, this.yaw, this.pitch);
    }

    public Location addYaw(float yaw) {
        return new Location(this.world, this.x, this.y, this.z, this.yaw + yaw, this.pitch);
    }

    public Location addPitch(float pitch) {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch + pitch);
    }

    public Location add(double x, double y, double z) {
        return new Location(this.world, this.x + x, this.y + y, this.z + z, this.yaw, this.pitch);
    }

    public Location add(double x, double y, double z, float yaw, float pitch) {
        return new Location(this.world, this.x + x, this.y + y, this.z + z, this.yaw + yaw, this.pitch + pitch);
    }

    public Location toCenter() {
        return new Location(this.world, ((int) Math.floor(this.x)) + 0.5, (int) Math.floor(this.y), ((int) Math.floor(this.z)) + 0.5, this.yaw, this.pitch);
    }

    public BlockPosition toBlockPos() {
        return new BlockPosition((int) Math.floor(this.x), (int) Math.floor(this.y), (int) Math.floor(this.z));
    }

    public org.bukkit.Location toBukkit() {
        return new org.bukkit.Location(this.world.toBukkit(), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public Location copy() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    @Override
    public String toString() {
        return "Location{" +
                "world=" + this.world +
                ", x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                ", yaw=" + this.yaw +
                ", pitch=" + this.pitch +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Location location)) {
            return false;
        }

        return Double.compare(this.getX(), location.getX()) == 0 && Double.compare(this.getY(), location.getY()) == 0 && Double.compare(this.getZ(), location.getZ()) == 0 && Float.compare(this.getYaw(), location.getYaw()) == 0 && Float.compare(this.getPitch(), location.getPitch()) == 0 && Objects.equals(this.getWorld(), location.getWorld());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.getWorld());
        result = 31 * result + Double.hashCode(this.getX());
        result = 31 * result + Double.hashCode(this.getY());
        result = 31 * result + Double.hashCode(this.getZ());
        result = 31 * result + Float.hashCode(this.getYaw());
        result = 31 * result + Float.hashCode(this.getPitch());
        return result;
    }

}
