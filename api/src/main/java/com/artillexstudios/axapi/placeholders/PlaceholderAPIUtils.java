package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.reflection.ClassUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

public enum PlaceholderAPIUtils {
    INSTANCE;
    private MethodHandle offlinePlayerSetPlaceholders;
    private MethodHandle playerSetPlaceholders;

    PlaceholderAPIUtils() {
        try {
            offlinePlayerSetPlaceholders = MethodHandles.publicLookup().findStatic(ClassUtils.INSTANCE.getClass("me.clip.placeholderapi.PlaceholderAPI"), "setPlaceholders", MethodType.methodType(String.class, List.of(OfflinePlayer.class, String.class)));
            playerSetPlaceholders = MethodHandles.publicLookup().findStatic(ClassUtils.INSTANCE.getClass("me.clip.placeholderapi.PlaceholderAPI"), "setPlaceholders", MethodType.methodType(String.class, List.of(Player.class, String.class)));
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            offlinePlayerSetPlaceholders = null;
            playerSetPlaceholders = null;
        }
    }

    public String setPlaceholders(Player player, String string) {
        if (playerSetPlaceholders != null) {
            try {
                return (String) playerSetPlaceholders.invoke(player, string);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return string;
    }

    public String setPlaceholders(OfflinePlayer player, String string) {
        if (offlinePlayerSetPlaceholders != null) {
            try {
                return (String) offlinePlayerSetPlaceholders.invoke(player, string);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return string;
    }
}
