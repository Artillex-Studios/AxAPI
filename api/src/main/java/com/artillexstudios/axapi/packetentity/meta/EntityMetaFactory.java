package com.artillexstudios.axapi.packetentity.meta;

import com.artillexstudios.axapi.packetentity.meta.entity.AreaEffectCloudMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.ArmorStandMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.ItemEntityMeta;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.function.Function;

public class EntityMetaFactory {
    private static final HashMap<EntityType, Function<Metadata,EntityMeta>> entityMeta = new HashMap<>();

    static {
        register(EntityType.DROPPED_ITEM, ItemEntityMeta::new);
        register(EntityType.AREA_EFFECT_CLOUD, AreaEffectCloudMeta::new);
        register(EntityType.ARMOR_STAND, ArmorStandMeta::new);
    }

    public static void register(EntityType type, Function<Metadata,EntityMeta> generator) {
        entityMeta.put(type, generator);
    }

    public static EntityMeta getForType(EntityType type) {
        return entityMeta.getOrDefault(type, EntityMeta::new).apply(new Metadata());
    }
}
