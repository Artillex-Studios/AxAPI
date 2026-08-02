package com.artillexstudios.axapi.nms.v1_20_R2.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.ItemLore;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;

import java.util.ArrayList;
import java.util.List;

public final class LoreDataComponent implements DataComponentHandler<ItemLore> {

    @Override
    public void setData(ItemStack from, ItemLore data) {
        CompoundTag tag = this.getDisplayTag(from);
        if (tag == null) {
            return;
        }

        ListTag listTag = new ListTag();
        List<String> jsonLore = ComponentSerializer.INSTANCE.toGsonList(data.lines());

        for (int i = 0; i < jsonLore.size(); i++) {
            listTag.add(StringTag.valueOf(jsonLore.get(i)));
        }

        this.setDisplayTag(tag, "Lore", listTag);
    }

    @Override
    public ItemLore getData(ItemStack stack) {
        CompoundTag tag = this.getDisplayTag(stack);
        if (tag == null) {
            return ItemLore.create(List.of());
        }

        if (!tag.contains("Lore")) {
            return ItemLore.create(List.of());
        }

        ListTag list = tag.getList("Lore", CraftMagicNumbers.NBT.TAG_STRING);
        ArrayList<String> lore = new ArrayList<>(list.size());
        for (int index = 0; index < list.size(); index++) {
            String line = list.getString(index);
            lore.add(line);
        }

        return ItemLore.create(ComponentSerializer.INSTANCE.fromGsonList(lore));
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getDisplayTag(itemStack);
        if (tag == null) {
            return;
        }

        this.setDisplayTag(tag, "Lore", null);
    }
}
