package com.artillexstudios.axapi.items.components.data;

import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import org.bukkit.enchantments.Enchantment;

public record ItemEnchantments(Object2IntMap<Enchantment> enchantments) {

    public ItemEnchantments {
        enchantments = Object2IntMaps.unmodifiable(enchantments);
    }

    public ItemEnchantments setLevel(Enchantment enchantment, int level) {
        Object2IntMap<Enchantment> enchantments = new Object2IntAVLTreeMap<>(this.enchantments);
        enchantments.put(enchantment, level);
        return new ItemEnchantments(enchantments);
    }

    public ItemEnchantments remove(Enchantment enchantment) {
        Object2IntMap<Enchantment> enchantments = new Object2IntAVLTreeMap<>(this.enchantments);
        enchantments.removeInt(enchantment);
        return new ItemEnchantments(enchantments);
    }


    public int getLevel(Enchantment enchantment) {
        return this.enchantments.getInt(enchantment);
    }
}
