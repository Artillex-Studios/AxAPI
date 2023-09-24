package com.artillexstudios.axapi.entity.impl;

import com.artillexstudios.axapi.utils.EquipmentSlot;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface PacketEntity {

    void setName(Component name, Player player);

    Component getName();

    void setName(Component name);

    Component getName(Player player);

    void teleport(Location location);

    void teleport(Location location, Player player);

    void setInvisible(boolean invisible);

    void setSilent(boolean silent);

    Location getLocation();

    Location getLocation(Player player);

    int getViewDistance();

    void setViewDistance(int blocks);

    int getViewDistanceSquared();

    void show(Player player);

    void hide(Player player);

    void forceHide(Player player);

    void remove();

    void setItem(EquipmentSlot slot, @NotNull ItemStack item);

    @Nullable
    ItemStack getItem(EquipmentSlot equipmentSlot);

    void clear(Player player);

    Set<Player> getViewers();

    int getEntityId();
}
