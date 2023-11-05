package com.artillexstudios.axapi.selection;

import org.bukkit.World;

public class Cuboid {
    private final int maxX;
    private final int minX;
    private final int maxZ;
    private final int minZ;
    private final int maxY;
    private final int minY;
    private final World world;

    public Cuboid(World world, int x1, int x2, int z1, int z2, int y1, int y2) {
        this.world = world;
        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);
        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);
        this.minY = Math.min(y1, y2);
        this.maxY = Math.max(y1, y2);
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinY() {
        return minY;
    }

    public World getWorld() {
        return world;
    }
}
