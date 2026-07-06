package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.scheduler.Scheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public enum ContainerUtils {
    INSTANCE;

    public void addOrDrop(Inventory inventory, List<ItemStack> items, Location location) {
        Location copy = location.clone();
        Scheduler.get().runAt(copy, () -> {
            for (ItemStack key : items) {
                HashMap<Integer, ItemStack> remaining = inventory.addItem(key);
                remaining.forEach((k, v) -> {
                    copy.getWorld().dropItem(copy, v);
                });
            }
        });
    }

    public void addOrDrop(Inventory inventory, ItemStack item, Location location) {
        this.addOrDrop(inventory, List.of(item), location);
    }

    public void addOrDrop(Player player, List<ItemStack> stacks) {
        Scheduler.get().run(player, task -> {
            Location location = player.getLocation();
            for (ItemStack stack : stacks) {
                HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(stack);
                remaining.forEach((k, v) -> {
                    location.getWorld().dropItem(location, v);
                });
            }
        }, () -> {});
    }

    public void addOrDrop(Player player, ItemStack stack) {
        this.addOrDrop(player, List.of(stack));
    }
}
