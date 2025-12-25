package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

public final class CustomDataDataComponent implements DataComponentHandler<CompoundTag, CustomData> {

    @Override
    public CustomData toNMS(CompoundTag from) {
        return CustomData.of(((com.artillexstudios.axapi.nms.v1_21_R7_paper.items.nbt.CompoundTag) from).getParent());
    }

    @Override
    public CompoundTag fromNMS(CustomData data) {
        return new com.artillexstudios.axapi.nms.v1_21_R7_paper.items.nbt.CompoundTag(data.getUnsafe());
    }
}
