package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface

WrappedItemStack {

    static WrappedItemStack wrap(ItemStack itemStack) {
        return NMSHandlers.getNmsHandler().wrapItem(itemStack);
    }

    static <T> T edit(ItemStack itemStack, Function<WrappedItemStack, T> function) {
        WrappedItemStack wrapped = wrap(itemStack);
        T result = function.apply(wrapped);
        wrapped.finishEdit();
        return result;
    }

    void setName(Component name);

    Component getName();

    void setLore(List<Component> lore);

    List<Component> getLore();

    void setAmount(int amount);

    int getAmount();

    void setCustomModelData(int customModelData);

    int getCustomModelData();

    void setMaterial(Material material);

    Map<Enchantment, Integer> getEnchantments();

    int getEnchantmentLevel(Enchantment enchantment);

    void addItemFlags(ItemFlag... itemFlags);

    void removeItemFlags(ItemFlag... itemFlags);

    Set<ItemFlag> getItemFlags();

    boolean hasItemFlag(ItemFlag itemFlag);

    CompoundTag getCompoundTag();

    ItemStack toBukkit();

    void finishEdit();

    Object getParent();
}
