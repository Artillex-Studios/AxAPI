package com.artillexstudios.axapi.items.component;

import org.bukkit.enchantments.Enchantment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemEnchantments {
    private final HashMap<Enchantment, Integer> enchantments;
    private final boolean showInTooltip;

    public ItemEnchantments(HashMap<Enchantment, Integer> enchantments, boolean showInTooltip) {
        this.enchantments = enchantments;
        this.showInTooltip = showInTooltip;
    }

    public ItemEnchantments remove(Enchantment enchantment) {
        HashMap<Enchantment, Integer> copy = (HashMap<Enchantment, Integer>) this.enchantments.clone();
        copy.remove(enchantment);
        return new ItemEnchantments(copy, showInTooltip);
    }

    public ItemEnchantments add(Enchantment enchantment, int level) {
        if (level <= 0) {
            return remove(enchantment);
        }

        HashMap<Enchantment, Integer> copy = (HashMap<Enchantment, Integer>) this.enchantments.clone();
        copy.put(enchantment, Math.min(level, 255));
        return new ItemEnchantments(copy, showInTooltip);
    }

    public boolean showInTooltip() {
        return showInTooltip;
    }

    public ItemEnchantments withTooltip(boolean showInTooltip) {
        return new ItemEnchantments(this.enchantments, showInTooltip);
    }

    public Set<Enchantment> keySet() {
        return Collections.unmodifiableSet(this.enchantments.keySet());
    }

    public Set<Map.Entry<Enchantment, Integer>> entrySet() {
        return Collections.unmodifiableSet(this.enchantments.entrySet());
    }

    public int getLevel(Enchantment enchantment) {
        return this.enchantments.get(enchantment);
    }

    public int size() {
        return this.enchantments.size();
    }

    public boolean isEmpty() {
        return this.enchantments.isEmpty();
    }
}
