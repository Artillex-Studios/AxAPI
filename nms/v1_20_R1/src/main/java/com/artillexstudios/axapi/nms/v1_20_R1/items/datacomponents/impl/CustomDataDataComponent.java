package com.artillexstudios.axapi.nms.v1_20_R1.items.datacomponents.impl;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class CustomDataDataComponent implements DataComponentHandler<CompoundTag> {

    @Override
    public void setData(ItemStack from, CompoundTag data) {
        from.setTag((net.minecraft.nbt.CompoundTag) data.getParent());
    }

    @Override
    public CompoundTag getData(ItemStack stack) {
        net.minecraft.nbt.CompoundTag tag = this.getTag(stack);
        return tag == null ? null : new com.artillexstudios.axapi.nms.v1_20_R1.items.nbt.CompoundTag(tag);
    }

    @Override
    public void removeData(ItemStack itemStack) {
        itemStack.setTag(null);
    }
}
