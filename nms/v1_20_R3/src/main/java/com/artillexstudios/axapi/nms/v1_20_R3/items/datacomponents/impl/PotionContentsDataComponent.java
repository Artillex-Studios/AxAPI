package com.artillexstudios.axapi.nms.v1_20_R3.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.PotionContents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R3.potion.CraftPotionType;

import java.util.List;
import java.util.Optional;

public final class PotionContentsDataComponent implements DataComponentHandler<PotionContents> {

    @Override
    public void setData(ItemStack from, PotionContents data) {
        net.minecraft.nbt.CompoundTag tag = this.getOrCreateTag(from);
        tag.putString("Potion", CraftPotionType.bukkitToString(data.type().orElseThrow()));
    }

    @Override
    public PotionContents getData(ItemStack stack) {
        CompoundTag tag = this.getTag(stack);
        if (tag == null) {
            return null;
        }

        return new PotionContents(Optional.ofNullable(CraftPotionType.stringToBukkit(tag.getString("Potion"))), Optional.empty(), List.of(), Optional.empty());
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getTag(itemStack);
        if (tag == null) {
            return;
        }

        tag.remove("Potion");
    }
}
