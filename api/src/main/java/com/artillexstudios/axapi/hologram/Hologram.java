package com.artillexstudios.axapi.hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface Hologram {

    List<HologramLine<?>> getLines();

    @NotNull
    UUID getID();

    <T> void addLine(@NotNull T content);

    <T> void setLine(int line, @NotNull T content);

    @Nullable
    HologramLine<?> getLine(int line);

    @NotNull
    Location getLocation();

    void teleport(@NotNull Location location);

    void show(@NotNull Player player);

    void hide(@NotNull Player player);

    void remove();

    void removeLine(int line);
}
