package com.artillexstudios.axapi.nms.v1_20_R1.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.ItemEnchantments;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.bukkit.craftbukkit.v1_20_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;

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
            enchantments.put(CraftEnchantment.getRaw(entry.getKey()), entry.getIntValue());
        }

        EnchantmentHelper.setEnchantments(enchantments, from);
    }

    @Override
    public ItemEnchantments getData(ItemStack stack) {
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        Object2IntMap<org.bukkit.enchantments.Enchantment> enchantments = new Object2IntAVLTreeMap<>();
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            enchantments.put(minecraftToBukkit(entry.getKey()), (int) entry.getValue());
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

    private static org.bukkit.enchantments.Enchantment minecraftToBukkit(Enchantment minecraft) {
        Preconditions.checkArgument(minecraft != null);
        Registry<Enchantment> registry = BuiltInRegistries.ENCHANTMENT;
        org.bukkit.enchantments.Enchantment bukkit = org.bukkit.Registry.ENCHANTMENT.get(CraftNamespacedKey.fromMinecraft((registry.getResourceKey(minecraft).orElseThrow()).location()));
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }
}
