package com.artillexstudios.axapi.nms.v1_20_R1.hologram;

import com.artillexstudios.axapi.entity.PacketEntityFactory;
import com.artillexstudios.axapi.entity.impl.PacketArmorStand;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.utils.FeatureFlags;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentHologramLine extends com.artillexstudios.axapi.hologram.impl.ComponentHologramLine {
    private final PacketArmorStand packetArmorStand;
    private final List<Placeholder> placeholders = new ArrayList<>();
    private boolean containsPlaceholders = false;

    public ComponentHologramLine(Location location) {
        packetArmorStand = (PacketArmorStand) PacketEntityFactory.get().spawnEntity(location, EntityType.ARMOR_STAND, (entity) -> {
            entity.setInvisible(true);
            ((PacketArmorStand) entity).setMarker(true);
        });
    }

    @Override
    public void set(@NotNull Component content) {
        String stringContent = PlainTextComponentSerializer.plainText().serialize(content);
        if (!stringContent.isBlank()) {
            packetArmorStand.setName(content);

            for (Pattern pattern : FeatureFlags.PLACEHOLDER_PATTERNS.get()) {
                Matcher matcher = pattern.matcher(stringContent);
                if (matcher.find()) {
                    containsPlaceholders = true;
                    return;
                }
            }
        } else {
            packetArmorStand.setName(null);
            containsPlaceholders = false;
        }
    }

    @NotNull
    @Override
    public Component get() {
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
        Holograms.remove(packetArmorStand.getEntityId());
    }

    @Override
    public Set<Player> getViewers() {
        return this.packetArmorStand.getViewers();
    }

    @Override
    public void addPlaceholder(Placeholder placeholder) {
        placeholders.add(placeholder);
    }

    @Override
    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }

    @Override
    public PacketEntity getEntity() {
        return packetArmorStand;
    }

    @Override
    public boolean containsPlaceholders() {
        return containsPlaceholders;
    }
}
