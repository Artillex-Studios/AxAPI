package com.artillexstudios.axapi.nms.v1_20_R3.items.datacomponents.impl;

import com.artillexstudios.axapi.items.WrappedItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public interface DataComponentHandler<T> {
    String DISPLAY_TAG = "display";

    default void apply(WrappedItemStack stack, T data) {
        ItemStack wrapped = ((com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack) stack).itemStack;
        if (data == null) {
            this.removeData(wrapped);
        } else {
            this.setData(wrapped, data);
        }
    }

    default T getData(WrappedItemStack stack) {
        ItemStack wrapped = ((com.artillexstudios.axapi.nms.v1_20_R3.items.WrappedItemStack) stack).itemStack;
        return this.getData(wrapped);
    }

    void setData(ItemStack from, T data);

    T getData(ItemStack stack);

    void removeData(ItemStack itemStack);

    @Nullable
    default CompoundTag getTag(ItemStack stack) {
        return stack.getTag();
    }

    default CompoundTag getOrCreateTag(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    @Nullable
    default CompoundTag getDisplayTag(ItemStack stack) {
        CompoundTag compoundTag = this.getTag(stack);
        if (compoundTag == null) {
            return null;
        }

        if (!compoundTag.contains(DISPLAY_TAG)) {
            return null;
        }

        return compoundTag.getCompound(DISPLAY_TAG);
    }

   default void setDisplayTag(CompoundTag compoundTag, String key, Tag value) {
        final CompoundTag display = compoundTag.getCompound(DISPLAY_TAG);

        if (!compoundTag.contains(DISPLAY_TAG)) {
            compoundTag.put(DISPLAY_TAG, display);
        }

        display.remove(key);

        if (value != null) {
            display.put(key, value);
        }
    }
}
