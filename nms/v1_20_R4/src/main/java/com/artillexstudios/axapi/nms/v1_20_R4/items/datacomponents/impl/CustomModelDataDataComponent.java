package com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.CustomModelData;

import java.util.List;

public final class CustomModelDataDataComponent implements DataComponentHandler<CustomModelData, net.minecraft.world.item.component.CustomModelData> {

    @Override
    public net.minecraft.world.item.component.CustomModelData toNMS(CustomModelData from) {
        return new net.minecraft.world.item.component.CustomModelData(from.floats().stream().findFirst().orElseThrow().intValue());
    }

    @Override
    public CustomModelData fromNMS(net.minecraft.world.item.component.CustomModelData data) {
        return new CustomModelData(List.of((float) data.value()), List.of(), List.of(), List.of());
    }
}
