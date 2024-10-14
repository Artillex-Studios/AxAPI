package com.artillexstudios.axapi.packetentity;

import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.function.Consumer;

public interface PacketEntity {
    Cache<Object, String> legacyCache = Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterAccess(Duration.ofSeconds(20))
            .scheduler(Scheduler.systemScheduler())
            .build();
    Cache<String, Object> componentCache = Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterAccess(Duration.ofSeconds(20))
            .scheduler(Scheduler.systemScheduler())
            .build();
    LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    void teleport(Location location);

    Location location();

    /**
     * Returns a modifyable meta instance
     * @return Entity metadata instance
     */
    EntityMeta meta();

    int id();

    int viewDistanceSquared();

    void viewDistance(int blocks);

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

    void onInteract(Consumer<PacketEntityInteractEvent> event);

    void callInteract(PacketEntityInteractEvent event);

    void ride(int entityId);

    void unRide(int entityId);

    void rotate(float yaw, float pitch);

    void rotateHead(float yaw);

    void update();
}
