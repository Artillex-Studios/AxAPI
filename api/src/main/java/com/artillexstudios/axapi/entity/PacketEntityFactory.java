package com.artillexstudios.axapi.entity;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.function.Consumer;

public interface PacketEntityFactory {

    static PacketEntityFactory get() {
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
