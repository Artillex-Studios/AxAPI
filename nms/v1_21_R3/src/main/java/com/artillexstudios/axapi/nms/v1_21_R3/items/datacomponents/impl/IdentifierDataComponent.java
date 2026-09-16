package com.artillexstudios.axapi.nms.v1_21_R3.items.datacomponents.impl;

import com.artillexstudios.axapi.nms.v1_21_R3.items.datacomponents.impl.DataComponentHandler;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;

public final class IdentifierDataComponent implements DataComponentHandler<Key, ResourceLocation> {

    @Override
    public ResourceLocation toNMS(Key from) {
        return ResourceLocation.fromNamespaceAndPath(from.namespace(), from.value());
    }

    @Override
    public Key fromNMS(ResourceLocation data) {
        //noinspection PatternValidation
        return Key.key(data.getNamespace(), data.getPath());
    }
}
