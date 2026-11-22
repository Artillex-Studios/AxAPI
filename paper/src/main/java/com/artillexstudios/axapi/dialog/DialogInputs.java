package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.dialog.action.CustomAllAction;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DialogInputs {
    private static final Map<String, CustomAllAction.DialogDataConsumer> cache = new ConcurrentHashMap<>();

    public static void register(String key, CustomAllAction.DialogDataConsumer consumer) {
        cache.put(key, consumer);
    }

    public static void call(Player player, String key, CompoundTag tag) {
        CustomAllAction.DialogDataConsumer dialogDataConsumer = cache.remove(key);
        if (dialogDataConsumer == null) {
            return;
        }

        dialogDataConsumer.call(player, tag);
    }
}
