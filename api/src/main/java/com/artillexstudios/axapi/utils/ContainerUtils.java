package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.scheduler.Scheduler;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public enum ContainerUtils {
    INSTANCE;

    public void addOrDrop(Inventory inventory, List<ItemStack> items, Location location) {
        Scheduler.get().runAt(location, () -> {
            for (ItemStack key : items) {
                HashMap<Integer, ItemStack> remaining = inventory.addItem(key);
                remaining.forEach((k, v) -> location.getWorld().dropItem(location, v));
            }
        });
    }
}
