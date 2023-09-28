package com.artillexstudios.axapi.entity;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEntityTracker {
    private static final Int2ObjectAVLTreeMap<PacketEntity> entities = new Int2ObjectAVLTreeMap<>();
    private static final Object mutex = new Object();

    public static void tickAll() {
        synchronized (mutex) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (PacketEntity entity : entities.values()) {
                    if (!entity.getLocation().getWorld().getUID().equals(player.getLocation().getWorld().getUID())) {
                        entity.hide(player);
                        continue;
                    }

                    if (player.getLocation().distanceSquared(entity.getLocation()) <= entity.getViewDistanceSquared()) {
                        try {
                            entity.show(player);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        entity.hide(player);
                    }
                }
            }
        }
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
