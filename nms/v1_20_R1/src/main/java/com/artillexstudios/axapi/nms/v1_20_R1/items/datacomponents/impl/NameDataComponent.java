package com.artillexstudios.axapi.nms.v1_20_R1.items.datacomponents.impl;

import com.artillexstudios.axapi.utils.ComponentSerializer;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

public final class NameDataComponent implements DataComponentHandler<Component> {

    @Override
    public void setData(ItemStack from, Component data) {
        CompoundTag tag = this.getOrCreateTag(from);
        this.setDisplayTag(tag, "Name", StringTag.valueOf(ComponentSerializer.INSTANCE.toGson(data)));
    }

    @Override
    public Component getData(ItemStack stack) {
        CompoundTag tag = this.getDisplayTag(stack);
        if (tag == null) {
            return null;
        }

        if (!tag.contains("Name")) {
            return null;
        }

        return ComponentSerializer.INSTANCE.fromGson(tag.getString("Name"));
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getDisplayTag(itemStack);
        if (tag == null) {
            return;
        }

        this.setDisplayTag(tag, "Name", null);
    }
}
