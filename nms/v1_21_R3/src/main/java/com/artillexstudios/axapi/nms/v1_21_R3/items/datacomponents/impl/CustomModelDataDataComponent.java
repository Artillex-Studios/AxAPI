package com.artillexstudios.axapi.nms.v1_21_R3.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.CustomModelData;
import com.artillexstudios.axapi.nms.v1_21_R3.items.datacomponents.impl.DataComponentHandler;

public final class CustomModelDataDataComponent implements DataComponentHandler<CustomModelData, net.minecraft.world.item.component.CustomModelData> {

    @Override
    public net.minecraft.world.item.component.CustomModelData toNMS(CustomModelData from) {
        return new net.minecraft.world.item.component.CustomModelData(from.floats(), from.flags(), from.strings(), from.colors());
    }

    @Override
    public CustomModelData fromNMS(net.minecraft.world.item.component.CustomModelData data) {
        return new CustomModelData(data.floats(), data.flags(), data.strings(), data.colors());
    }
}
