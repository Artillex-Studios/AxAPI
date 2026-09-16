package com.artillexstudios.axapi.nms.v1_20_R2.items.datacomponents.impl;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.bukkit.inventory.ItemFlag;

import java.util.Map;

public final class EnchantmentGlintOverrideDataComponent implements DataComponentHandler<Boolean> {

    @Override
    public void setData(ItemStack from, Boolean data) {
        Map<net.minecraft.world.item.enchantment.Enchantment, Integer> enchantments = Map.of(Enchantments.LOYALTY, 1);
        EnchantmentHelper.setEnchantments(enchantments, from);
        addItemFlags(this.getOrCreateTag(from), ItemFlag.HIDE_ENCHANTS);
    }

    private static void addItemFlags(net.minecraft.nbt.CompoundTag tag, ItemFlag... itemFlags) {
        byte flag = tag.contains("HideFlags", 99) ? (byte) tag.getInt("HideFlags") : (byte) 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag |= getBitModifier(itemFlag);
        }

        tag.putInt("HideFlags", flag);
    }

    private static byte getBitModifier(ItemFlag hideFlag) {
        return (byte) (1 << hideFlag.ordinal());
    }

    @Override
    public Boolean getData(ItemStack stack) {
        return null;
    }

    @Override
    public void removeData(ItemStack itemStack) {

    }
}
