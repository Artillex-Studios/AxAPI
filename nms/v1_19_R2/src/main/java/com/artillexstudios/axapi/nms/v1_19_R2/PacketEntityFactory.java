package com.artillexstudios.axapi.nms.v1_19_R2;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.nms.v1_19_R2.entity.PacketArmorStand;
import com.artillexstudios.axapi.nms.v1_19_R2.entity.PacketItem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class PacketEntityFactory implements com.artillexstudios.axapi.entity.PacketEntityFactory {

    @Override
    public PacketEntity spawnEntity(Location location, EntityType entityType) {
        PacketEntity entity;
        if (entityType == EntityType.ARMOR_STAND) {
            entity = new PacketArmorStand(location);
        } else if (entityType == EntityType.DROPPED_ITEM) {
            entity = new PacketItem(location);
        } else {
            entity = new com.artillexstudios.axapi.nms.v1_19_R2.entity.PacketEntity(entityType, location);
        }

        return entity;
    }
}
