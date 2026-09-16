package com.artillexstudios.axapi.nms.v1_20_R2.items.datacomponents.impl;

import com.artillexstudios.axapi.items.component.type.Unit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class UnbreakableDataComponent implements DataComponentHandler<Unit> {

    @Override
    public void setData(ItemStack from, Unit data) {
        CompoundTag tag = this.getOrCreateTag(from);
        tag.putBoolean("Unbreakable", true);
    }

    @Override
    public Unit getData(ItemStack stack) {
        CompoundTag tag = this.getTag(stack);
        if (tag == null || !tag.contains("Unbreakable")) {
            return null;
        }

        return tag.getBoolean("Unbreakable") ? Unit.INSTANCE : null;
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getTag(itemStack);
        if (tag == null || !tag.contains("Unbreakable")) {
            return;
        }

        tag.remove("Unbreakable");
    }
}