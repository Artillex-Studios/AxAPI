package com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl;

import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;

public final class IdentifierDataComponent implements DataComponentHandler<Key, ResourceLocation> {

    @Override
    public ResourceLocation toNMS(Key from) {
        return ResourceLocation.tryBuild(from.namespace(), from.value());
    }

    @Override
    public Key fromNMS(ResourceLocation data) {
        //noinspection PatternValidation
        return Key.key(data.getNamespace(), data.getPath());
    }
}
