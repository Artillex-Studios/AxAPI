package com.artillexstudios.axapi.items.component.type;

import org.bukkit.enchantments.Enchantment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ItemEnchantments {
    private final HashMap<Enchantment, Integer> enchantments;
    private final Set<Map.Entry<Enchantment, Integer>> entrySet;
    private final Set<Enchantment> keySet;
    private final boolean showInTooltip;

    public ItemEnchantments(HashMap<Enchantment, Integer> enchantments, boolean showInTooltip) {
        this.enchantments = enchantments;
        this.entrySet = Collections.unmodifiableSet(this.enchantments.entrySet());
        this.keySet = Collections.unmodifiableSet(this.enchantments.keySet());
        this.showInTooltip = showInTooltip;
    }

    public ItemEnchantments remove(Enchantment enchantment) {
        HashMap<Enchantment, Integer> copy = (HashMap<Enchantment, Integer>) this.enchantments.clone();
        copy.remove(enchantment);
        return new ItemEnchantments(copy, this.showInTooltip);
    }

    public ItemEnchantments add(Enchantment enchantment, int level) {
        if (level <= 0) {
            return this.remove(enchantment);
        }

        HashMap<Enchantment, Integer> copy = (HashMap<Enchantment, Integer>) this.enchantments.clone();
        copy.put(enchantment, Math.min(level, 255));
        return new ItemEnchantments(copy, this.showInTooltip);
    }

    public boolean showInTooltip() {
        return this.showInTooltip;
    }

    public ItemEnchantments withTooltip(boolean showInTooltip) {
        return new ItemEnchantments(this.enchantments, showInTooltip);
    }

    public Set<Enchantment> keySet() {
        return this.keySet;
    }

    public Set<Map.Entry<Enchantment, Integer>> entrySet() {
        return this.entrySet;
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
