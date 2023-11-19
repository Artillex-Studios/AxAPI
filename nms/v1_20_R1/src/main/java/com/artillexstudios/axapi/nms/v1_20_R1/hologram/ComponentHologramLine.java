package com.artillexstudios.axapi.nms.v1_20_R1.hologram;

import com.artillexstudios.axapi.entity.PacketEntityFactory;
import com.artillexstudios.axapi.entity.impl.PacketArmorStand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ComponentHologramLine extends com.artillexstudios.axapi.hologram.impl.ComponentHologramLine {
    private final PacketArmorStand packetArmorStand;

    public ComponentHologramLine(Location location) {
        packetArmorStand = (PacketArmorStand) PacketEntityFactory.get().spawnEntity(location, EntityType.ARMOR_STAND);
        packetArmorStand.setInvisible(true);
        packetArmorStand.setMarker(true);
    }

    @Override
    public void set(@NotNull Component content) {
        if (!PlainTextComponentSerializer.plainText().serialize(content).isBlank()) {
            packetArmorStand.setName(content);
        } else {
            packetArmorStand.setName(null);
        }
    }

    @Override
    public void set(@NotNull Component content, @NotNull Player player) {
        packetArmorStand.setName(content);
    }

    @NotNull
    @Override
    public Component get() {
        return packetArmorStand.getName();
    }

    @NotNull
    @Override
    public Component get(@NotNull Player player) {
        return packetArmorStand.getName();
    }

    @Override
    public void hide(@NotNull Player player) {
        packetArmorStand.hide(player);
    }

    @Override
    public void show(@NotNull Player player) {
        packetArmorStand.show(player);
    }

    @Override
    public void teleport(@NotNull Location location) {
        packetArmorStand.teleport(location);
    }

    @Override
    public void remove() {
        packetArmorStand.remove();
    }

    @Override
    public Set<Player> getViewers() {
        return this.packetArmorStand.getViewers();
    }
}
