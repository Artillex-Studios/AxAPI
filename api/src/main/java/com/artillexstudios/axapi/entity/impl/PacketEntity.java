package com.artillexstudios.axapi.entity.impl;

import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface PacketEntity {

    /**
     * Get the name of the entity.
     * @return Name of the entity.
     */
    Component getName();

    /**
     * Set the name of the entity.
     * @param name Name of the entity.
     */
    void setName(Component name);

    /**
     * Teleport the entity to a specified location.
     * @param location Location to teleport to.
     */
    void teleport(Location location);

    /**
     * Make the entity invisible to players. This sets a metadata,
     * the entity still tracks the player.
     * @param invisible If the entity should be invisible.
     */
    void setInvisible(boolean invisible);


    /**
     * Marks the entity as silent.
     * @param silent If the entity should emit noises.
     */
    void setSilent(boolean silent);

    /**
     * Get the current location of the entity.
     * @return Current location of the entity.
     */
    Location getLocation();

    int getViewDistance();

    void setViewDistance(int blocks);

    int getViewDistanceSquared();

    void show(Player player);

    void hide(Player player);

    /**
     * Removes the entity.
     */
    void remove();

    /**
     * Sets the item of the entity in the specified EquipmentSlot to the itemstack.
     */
    void setItem(EquipmentSlot slot, @Nullable ItemStack item);

    /**
     * Gets the item of the entity in the specified EquipmentSlot.
     */
    @Nullable
    ItemStack getItem(EquipmentSlot equipmentSlot);

    /**
     * Returns an immutable copy of the viewers of this entity.
     */
    Set<Player> getViewers();

    int getEntityId();

    void shouldSee(Predicate<Player> predicate);

    void onClick(Consumer<PacketEntityInteractEvent> event);

    void removeClickListener(Consumer<PacketEntityInteractEvent> eventConsumer);

    void ride(Entity entity);

    void ride(PacketEntity entity);

    void unRide();

    void sendMetaUpdate();
}
