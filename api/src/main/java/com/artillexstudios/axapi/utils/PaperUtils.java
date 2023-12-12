package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.utils.server.ServerImplementation;
import com.artillexstudios.axapi.utils.server.ServerImplementationPaper;
import com.artillexstudios.axapi.utils.server.ServerImplementationSpigot;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.CompletableFuture;

public class PaperUtils {
    private static final ServerImplementation IMPLEMENTATION;
    private static final boolean FOLIA;

    static {
        if (ClassUtils.classExists("io.papermc.paper.configuration.Configuration") || ClassUtils.classExists("com.destroystokyo.paper.PaperConfig")) {
            IMPLEMENTATION = new ServerImplementationPaper();
        } else {
            IMPLEMENTATION = new ServerImplementationSpigot();
        }

        FOLIA = ClassUtils.classExists("io.papermc.paper.threadedregions.RegionizedServer");
    }

    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location) {
        return IMPLEMENTATION.teleportAsync(entity, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        return IMPLEMENTATION.teleportAsync(entity, location, cause);
    }

    public static CompletableFuture<Chunk> getChunkAtAsync(Location location) {
        return IMPLEMENTATION.getChunkAtAsync(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, true);
    }

    public static CompletableFuture<Chunk> getChunkAtAsync(Location location, boolean gen) {
        return IMPLEMENTATION.getChunkAtAsync(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, gen);
    }

    public static CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z) {
        return IMPLEMENTATION.getChunkAtAsync(world, x, z, true);
    }

    public static CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
        return IMPLEMENTATION.getChunkAtAsync(world, x, z, gen, false);
    }

    public static CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent) {
        return IMPLEMENTATION.getChunkAtAsync(world, x, z, gen, isUrgent);
    }

    public static CompletableFuture<Chunk> getChunkAtAsyncUrgently(World world, int x, int z, boolean gen) {
        return IMPLEMENTATION.getChunkAtAsync(world, x, z, gen, true);
    }

    public static boolean isChunkGenerated(Location location) {
        return isChunkGenerated(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public static boolean isChunkGenerated(World world, int x, int z) {
        return IMPLEMENTATION.isChunkGenerated(world, x, z);
    }

    public static boolean isFolia() {
        return FOLIA;
    }
}
