package com.artillexstudios.axapi.nms.v1_18_R2;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketAreaEffectCloud;
import com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketArmorStand;
import com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketItem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.function.Consumer;

public class PacketEntityFactory implements com.artillexstudios.axapi.entity.PacketEntityFactory {

    @Override
    public PacketEntity spawnEntity(Location location, EntityType entityType, Consumer<PacketEntity> consumer) {
        PacketEntity entity;
        if (entityType == EntityType.ARMOR_STAND) {
            entity = new PacketArmorStand(location, consumer);
        } else if (entityType == EntityType.DROPPED_ITEM) {
            entity = new PacketItem(location, consumer);
        } else if (entityType == EntityType.AREA_EFFECT_CLOUD) {
            entity = new PacketAreaEffectCloud(location, consumer);
        } else {
            entity = new com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketEntity(entityType, location, consumer);
        }

        return entity;
    }
}
