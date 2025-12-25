package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import net.kyori.adventure.key.Key;
import net.minecraft.resources.Identifier;

public final class IdentifierDataComponent implements DataComponentHandler<Key, Identifier> {

    @Override
    public Identifier toNMS(Key from) {
        return Identifier.fromNamespaceAndPath(from.namespace(), from.value());
    }

    @Override
    public Key fromNMS(Identifier data) {
        //noinspection PatternValidation
        return Key.key(data.getNamespace(), data.getPath());
    }
}
