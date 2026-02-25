package com.artillexstudios.axapi.utils.server;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.concurrent.CompletableFuture;

public interface ServerImplementation {

    CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, TeleportCause teleportCause);

    default CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
        return getChunkAtAsync(world, x, z, gen, false);
    }

    CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent);

    boolean isChunkGenerated(World world, int x, int z);

    InventoryHolder getHolder(Inventory inventory, boolean useSnapshot);
}
