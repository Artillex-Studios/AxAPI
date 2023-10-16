package com.artillexstudios.axapi.entity.impl;

import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

public interface PacketEntity {

    Component getName();

    void setName(Component name);

    void teleport(Location location);

    void setInvisible(boolean invisible);

    void setSilent(boolean silent);

    Location getLocation();

    int getViewDistance();

    void setViewDistance(int blocks);

    int getViewDistanceSquared();

    void show(Player player);

    void hide(Player player);

    void remove();

    void setItem(EquipmentSlot slot, @Nullable ItemStack item);

    @Nullable
    ItemStack getItem(EquipmentSlot equipmentSlot);

    void clear(Player player);

    Set<Player> getViewers();

    int getEntityId();

    void onClick(Consumer<PacketEntityInteractEvent> event);

    void removeClickListener(Consumer<PacketEntityInteractEvent> eventConsumer);
}
