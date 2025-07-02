package com.artillexstudios.axapi.gui.inventory;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InventoryRenderers {
    private static final ConcurrentHashMap<UUID, InventoryRenderer> renderers = new ConcurrentHashMap<>();

    public static InventoryRenderer getRenderer(Player player) {
        return renderers.computeIfAbsent(player.getUniqueId(), uuid -> new InventoryRenderer(player));
    }

    public static void disconnect(UUID uuid) {
        renderers.remove(uuid);
    }
}
