package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface HologramLine<T> {

    void set(@NotNull T content);

    @NotNull
    T get();

    void hide(@NotNull Player player);

    void show(@NotNull Player player);

    void teleport(@NotNull Location location);

    void remove();

    Set<Player> getViewers();

    void addPlaceholder(Placeholder placeholder);

    List<Placeholder> getPlaceholders();

    PacketEntity getEntity();

    boolean containsPlaceholders();
}
