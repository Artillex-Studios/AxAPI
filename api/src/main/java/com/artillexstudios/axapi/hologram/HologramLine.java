package com.artillexstudios.axapi.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface HologramLine<T> {

    void set(@NotNull T content);

    void set(@NotNull T content, @NotNull Player player);

    @NotNull
    T get();

    @NotNull
    T get(@NotNull Player player);

    void hide(@NotNull Player player);

    void show(@NotNull Player player);

    void teleport(@NotNull Location location);

    void remove();

    Set<Player> getViewers();
}
