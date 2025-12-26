package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.ItemEnchantments;
import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public final class EnchantmentsDataComponent implements DataComponentHandler<ItemEnchantments, net.minecraft.world.item.enchantment.ItemEnchantments> {

    @Override
    public net.minecraft.world.item.enchantment.ItemEnchantments toNMS(ItemEnchantments from) {
        net.minecraft.world.item.enchantment.ItemEnchantments.Mutable enchantments = new net.minecraft.world.item.enchantment.ItemEnchantments.Mutable(net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
        for (Object2IntMap.Entry<Enchantment> entry : from.enchantments().object2IntEntrySet()) {
            enchantments.set(CraftEnchantment.bukkitToMinecraftHolder(entry.getKey()), entry.getIntValue());
        }

        return enchantments.toImmutable();
    }

    @Override
    public ItemEnchantments fromNMS(net.minecraft.world.item.enchantment.ItemEnchantments data) {
        Object2IntAVLTreeMap<Enchantment> enchantments = new Object2IntAVLTreeMap<>();
        for (Object2IntMap.Entry<Holder<net.minecraft.world.item.enchantment.@NotNull Enchantment>> entry : data.entrySet()) {
            enchantments.put(CraftEnchantment.minecraftHolderToBukkit(entry.getKey()), entry.getIntValue());
        }

        return new ItemEnchantments(enchantments);
    }
}
