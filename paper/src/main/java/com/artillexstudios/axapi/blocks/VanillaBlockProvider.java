package com.artillexstudios.axapi.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jspecify.annotations.NonNull;

public final class VanillaBlockProvider implements BlockProvider {

    @Override
    public @NonNull String getComparableBlock(@NonNull Location location) {
        return location.getBlock().getType().toString();
    }

    @Override
    public void placeBlock(@NonNull Location location, @NonNull String id) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        Material material = Material.getMaterial(id);
        if (material == null || !material.isBlock()) {
            return;
        }

        world.getBlockAt(location).setType(material);
    }
}
