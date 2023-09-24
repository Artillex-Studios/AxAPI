package com.artillexstudios.axapi.entity;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface PacketEntityFactory {

    static PacketEntityFactory get() {
        return NMSHandlers.getPacketEntityFactory();
    }

    PacketEntity spawnEntity(Location location, EntityType entityType);
}
