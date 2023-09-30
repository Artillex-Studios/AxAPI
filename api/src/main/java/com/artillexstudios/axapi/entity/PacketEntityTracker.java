package com.artillexstudios.axapi.entity;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

public class PacketEntityTracker {
    private static final Int2ObjectLinkedOpenHashMap<PacketEntity> entities = new Int2ObjectLinkedOpenHashMap<>();
    private static final Object mutex = new Object();

    public static void tickAll() {
        synchronized (mutex) {
            Location playerLocation = new Location(null, 0, 0, 0, 0, 0);
            for (Player player : Bukkit.getOnlinePlayers()) {
                // Reuse the same location object with the values changed, so we can avoid unnecessary heap churn
                player.getLocation(playerLocation);

                entities.values().forEach(entity -> {
                    if (!entity.getLocation().getWorld().equals(playerLocation.getWorld())) {
                        entity.hide(player);
                        return;
                    }

                    // We can use a dumbed-down version of Bukkit's distanceSquared, as we know that the worlds are the same
                    if (distanceSquared(playerLocation, entity.getLocation()) <= entity.getViewDistanceSquared()) {
                        try {
                            entity.show(player);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        entity.hide(player);
                    }
                });
            }
        }
    }

    public static double distanceSquared(Location location, Location other) {
        return NumberConversions.square(location.getX() - other.getX()) + NumberConversions.square(location.getY() - other.getY()) + NumberConversions.square(location.getZ() - other.getZ());
    }

    public static void startTracking(PacketEntity entity) {
        synchronized (mutex) {
            entities.put(entity.getEntityId(), entity);
        }
    }

    public static void stopTracking(PacketEntity entity) {
        synchronized (mutex) {
            entities.remove(entity.getEntityId());
        }
    }

    public static PacketEntity getById(int id) {
        synchronized (mutex) {
            return entities.get(id);
        }
    }

    public static void clearPlayer(Player player) {
        synchronized (mutex) {
            for (PacketEntity entity : entities.values()) {
                entity.clear(player);
            }
        }
    }
}
