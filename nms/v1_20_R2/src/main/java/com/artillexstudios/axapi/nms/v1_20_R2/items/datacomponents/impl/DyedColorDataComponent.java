package com.artillexstudios.axapi.nms.v1_20_R2.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.DyedItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class DyedColorDataComponent implements DataComponentHandler<DyedItemColor> {

    @Override
    public void setData(ItemStack from, DyedItemColor data) {
       CompoundTag tag = this.getOrCreateTag(from);
        if (from.getItem() == Items.LEATHER_BOOTS || from.getItem() == Items.LEATHER_LEGGINGS || from.getItem() == Items.LEATHER_CHESTPLATE || from.getItem() == Items.LEATHER_HELMET) {
            this.setDisplayTag(tag, "Color", IntTag.valueOf(data.rgb()));
        } else {
            tag.putInt("CustomPotionColor", data.rgb());
        }
    }

    @Override
    public DyedItemColor getData(ItemStack stack) {
       CompoundTag tag = this.getTag(stack);
        if (tag == null) {
            return null;
        }

        if (stack.getItem() == Items.LEATHER_BOOTS || stack.getItem() == Items.LEATHER_LEGGINGS || stack.getItem() == Items.LEATHER_CHESTPLATE || stack.getItem() == Items.LEATHER_HELMET) {
            return new DyedItemColor(tag.getInt("Color"));
        } else {
            return new DyedItemColor(tag.getInt("CustomPotionColor"));
        }
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getTag(itemStack);
        if (tag == null) {
            return;
        }

        if (itemStack.getItem() == Items.LEATHER_BOOTS || itemStack.getItem() == Items.LEATHER_LEGGINGS || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.LEATHER_HELMET) {
            tag.remove("Color");
        } else {
            tag.remove("CustomPotionColor");
        }
    }
}
