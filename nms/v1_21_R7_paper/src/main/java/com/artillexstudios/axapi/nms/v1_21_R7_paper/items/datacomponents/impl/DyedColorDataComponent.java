package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.DyedItemColor;

public final class DyedColorDataComponent implements DataComponentHandler<DyedItemColor, net.minecraft.world.item.component.DyedItemColor> {

    @Override
    public net.minecraft.world.item.component.DyedItemColor toNMS(DyedItemColor from) {
        return new net.minecraft.world.item.component.DyedItemColor(from.rgb());
    }

    @Override
    public DyedItemColor fromNMS(net.minecraft.world.item.component.DyedItemColor data) {
        return new DyedItemColor(data.rgb());
    }
}
