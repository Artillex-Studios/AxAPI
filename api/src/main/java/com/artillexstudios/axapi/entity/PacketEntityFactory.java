package com.artillexstudios.axapi.entity;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public interface PacketEntityFactory {
    static final Logger log = LoggerFactory.getLogger(PacketEntityFactory.class);

    static PacketEntityFactory get() {
        if (AxPlugin.tracker == null) {
            RuntimeException exception = new RuntimeException("Packet Entity system was not enabled!");
            log.error("Packet Entity system was not enabled! Set the PACKET_ENTITY_TRACKER_ENABLED feature flag to true!", exception);
            throw exception;
        }

        return NMSHandlers.getPacketEntityFactory();
    }

    /**
     * Spawn a PacketEntity at the specified location.
     * @param location Location to spawn the entity at
     * @param entityType The entitytype of the entity
     * @param consumer The consumer that runs just before the entity is spawned
     * @return The spawned entity
     */
    PacketEntity spawnEntity(Location location, EntityType entityType, Consumer<PacketEntity> consumer);

    default PacketEntity spawnEntity(Location location, EntityType entityType) {
        return spawnEntity(location, entityType, packetEntity -> {});
    }
}
