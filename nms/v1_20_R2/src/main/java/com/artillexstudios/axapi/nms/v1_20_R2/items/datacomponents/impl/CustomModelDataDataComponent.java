package com.artillexstudios.axapi.nms.v1_20_R2.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.CustomModelData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class CustomModelDataDataComponent implements DataComponentHandler<CustomModelData> {

    @Override
    public void setData(ItemStack from, CustomModelData data) {
        CompoundTag tag = this.getOrCreateTag(from);
        tag.putInt("CustomModelData", data.floats().getFirst().intValue());
    }

    @Override
    public CustomModelData getData(ItemStack stack) {
        CompoundTag tag = this.getTag(stack);
        if (tag == null || !tag.contains("CustomModelData")) {
            return null;
        }

        int customModelData = tag.getInt("CustomModelData");
        return new CustomModelData(List.of((float) customModelData), List.of(), List.of(), List.of());
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getTag(itemStack);
        if (tag == null) {
            return;
        }

        tag.remove("CustomModelData");
    }
}
