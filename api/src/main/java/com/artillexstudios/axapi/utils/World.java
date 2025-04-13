package com.artillexstudios.axapi.utils;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;

public class World {
    private final String name;

    public World(String name) {
        this.name = Preconditions.checkNotNull(name, "Can't create World with null name!");
    }

    public World(org.bukkit.World world) {
        Preconditions.checkNotNull(world, "Can't create World with null name!");
        this.name = world.getName();
    }

    public org.bukkit.World toBukkit() {
        return Bukkit.getWorld(this.name);
    }
}
