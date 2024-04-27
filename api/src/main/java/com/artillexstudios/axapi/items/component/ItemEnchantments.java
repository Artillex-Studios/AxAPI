package com.artillexstudios.axapi.items.component;

import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.enchantments.Enchantment;

import java.util.Collections;
import java.util.Set;

public class ItemEnchantments {
    private final Object2IntAVLTreeMap<Enchantment> enchantments;
    private final boolean showInTooltip;

    public ItemEnchantments(Object2IntAVLTreeMap<Enchantment> enchantments, boolean showInTooltip) {
        this.enchantments = enchantments;
        this.showInTooltip = showInTooltip;
    }

    public ItemEnchantments remove(Enchantment enchantment) {
        Object2IntAVLTreeMap<Enchantment> copy = this.enchantments.clone();
        copy.removeInt(enchantment);
        return new ItemEnchantments(copy, showInTooltip);
    }

    public ItemEnchantments add(Enchantment enchantment, int level) {
        if (level <= 0) {
            return remove(enchantment);
        }

        Object2IntAVLTreeMap<Enchantment> copy = this.enchantments.clone();
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

    public Set<Object2IntMap.Entry<Enchantment>> entrySet() {
        return Collections.unmodifiableSet(this.enchantments.object2IntEntrySet());
    }

    public int getLevel(Enchantment enchantment) {
        return this.enchantments.getInt(enchantment);
    }

    public int size() {
        return this.enchantments.size();
    }

    public boolean isEmpty() {
        return this.enchantments.isEmpty();
    }
}
