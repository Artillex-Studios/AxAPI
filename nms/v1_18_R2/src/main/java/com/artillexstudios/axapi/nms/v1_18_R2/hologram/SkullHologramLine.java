package com.artillexstudios.axapi.nms.v1_18_R2.hologram;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class SkullHologramLine extends com.artillexstudios.axapi.hologram.impl.SkullHologramLine {

    public SkullHologramLine(Location location) {

    }

    @Override
    public void set(@NotNull Skull content) {

    }

    @NotNull
    @Override
    public Skull get() {
        return null;
    }


    @Override
    public void hide(@NotNull Player player) {

    }

    @Override
    public void show(@NotNull Player player) {

    }

    @Override
    public void teleport(@NotNull Location location) {

    }

    @Override
    public void remove() {

    }

    @Override
    public Set<Player> getViewers() {
        return null;
    }

    @Override
    public void addPlaceholder(Placeholder placeholder) {

    }

    @Override
    public List<Placeholder> getPlaceholders() {
        return List.of();
    }

    @Override
    public PacketEntity getEntity() {
        return null;
    }
}
