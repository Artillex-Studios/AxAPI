package com.artillexstudios.axapi.packetentity;

import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.function.Consumer;

public interface PacketEntity {
    LoadingCache<String, Object> placeholderFormatCache = Caffeine.newBuilder()
            .maximumSize(FeatureFlags.HOLOGRAM_PARSED_LINE_CACHE.get())
            .expireAfterAccess(Duration.ofSeconds(20))
            .scheduler(Scheduler.systemScheduler())
            .build(parsed -> ComponentSerializer.INSTANCE.toVanilla(StringUtils.format(parsed)));

    void teleport(Location location);

    Location location();

    /**
     * Returns a modifiable meta instance
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

    int riddenEntity();

    void rotate(float yaw, float pitch);

    void rotateHead(float yaw);

    void update();
}
