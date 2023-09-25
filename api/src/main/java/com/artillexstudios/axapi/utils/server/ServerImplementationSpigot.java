package com.artillexstudios.axapi.utils.server;

import com.artillexstudios.axapi.utils.PaperUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.CompletableFuture;

public class ServerImplementationSpigot implements ServerImplementation {

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return CompletableFuture.completedFuture(entity.teleport(location, teleportCause));
    }

    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent) {
        if (!gen && !PaperUtils.isChunkGenerated(world, x, z)) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.completedFuture(world.getChunkAt(x, z));
    }

    @Override
    public boolean isChunkGenerated(World world, int x, int z) {
        return true;
    }
}
