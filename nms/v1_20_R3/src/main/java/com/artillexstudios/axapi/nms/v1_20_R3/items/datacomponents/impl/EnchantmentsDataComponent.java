package com.artillexstudios.axapi.nms.v1_20_R3.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.ItemEnchantments;
import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment;

import java.util.HashMap;
import java.util.Map;

public final class EnchantmentsDataComponent implements DataComponentHandler<ItemEnchantments> {
    private final String tagKey;

    public EnchantmentsDataComponent(String tagKey) {
        this.tagKey = tagKey;
    }

    @Override
    public void setData(ItemStack from, ItemEnchantments data) {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        for (Object2IntMap.Entry<org.bukkit.enchantments.Enchantment> entry : data.enchantments().object2IntEntrySet()) {
            enchantments.put(CraftEnchantment.bukkitToMinecraft(entry.getKey()), entry.getIntValue());
        }

        EnchantmentHelper.setEnchantments(enchantments, from);
    }

    @Override
    public ItemEnchantments getData(ItemStack stack) {
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        Object2IntMap<org.bukkit.enchantments.Enchantment> enchantments = new Object2IntAVLTreeMap<>();
        for (Map.Entry<net.minecraft.world.item.enchantment.Enchantment, Integer> entry : enchants.entrySet()) {
            enchantments.put(CraftEnchantment.minecraftToBukkit(entry.getKey()), (int) entry.getValue());
        }

        return new ItemEnchantments(enchantments);
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getTag(itemStack);
        if (tag == null) {
            return;
        }

        tag.remove(this.tagKey);
    }
}
