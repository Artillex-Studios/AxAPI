package com.artillexstudios.axapi.packetentity;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PacketEntity {

    void teleport(Location location);

    Location location();

    /**
     * Returns a modifyable meta instance
     * @return Entity metadata instance
     */
    EntityMeta meta();

    int id();

    /**
     * Adds the entity into our tracker
     */
    void spawn();

    void hide(Player player);

    void show(Player player);

    void setVisibleByDefault(boolean visible);

    void setItem(EquipmentSlot slot, WrappedItemStack item);

    WrappedItemStack getItem(EquipmentSlot slot);

    void sendChanges();

    void removePairing(Player player);

    void addPairing(Player player);

    boolean canSee(Player player);

    void remove();
}
