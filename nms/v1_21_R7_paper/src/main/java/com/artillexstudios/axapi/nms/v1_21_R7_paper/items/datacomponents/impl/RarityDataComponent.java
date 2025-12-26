package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.Rarity;

public final class RarityDataComponent implements DataComponentHandler<Rarity, net.minecraft.world.item.Rarity> {

    @Override
    public net.minecraft.world.item.Rarity toNMS(Rarity from) {
        return net.minecraft.world.item.Rarity.valueOf(from.name());
    }

    @Override
    public Rarity fromNMS(net.minecraft.world.item.Rarity data) {
        return Rarity.valueOf(data.name());
    }
}
