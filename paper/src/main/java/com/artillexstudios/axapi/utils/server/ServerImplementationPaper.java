package com.artillexstudios.axapi.utils.server;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.concurrent.CompletableFuture;

public class ServerImplementationPaper implements ServerImplementation {

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return entity.teleportAsync(location, teleportCause);
    }

    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent) {
        return world.getChunkAtAsync(x, z, gen, isUrgent);
    }

    @Override
    public boolean isChunkGenerated(World world, int x, int z) {
        return world.isChunkGenerated(x, z);
    }

    @Override
    public InventoryHolder getHolder(Inventory inventory, boolean useSnapshot) {
        return inventory.getHolder(useSnapshot);
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, int size, Component title) {
        return Bukkit.createInventory(holder, size, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, Component title) {
        return Bukkit.createInventory(holder, type, title);
    }
}
