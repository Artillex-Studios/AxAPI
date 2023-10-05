package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(@NotNull Section section) {
        this.itemStack = new ItemStack(getMaterial(section.getString("type", "stone")));
        section.getOptionalString("texture").ifPresent(this::setTextureValue);
        section.getOptionalString("name").ifPresent(name -> setName(StringUtils.formatToString(name)));
        section.getOptionalStringList("lore").ifPresent(name -> setLore(StringUtils.formatListToString(name)));
        section.getOptionalInt("custom-model-data").ifPresent(this::setCustomModelData);
        section.getOptionalInt("amount").ifPresent(this::amount);
        section.getOptionalBoolean("glow").ifPresent(this::glow);
        section.getOptionalStringList("enchants").ifPresent(enchants -> addEnchants(createEnchantmentsMap(enchants)));
        section.getOptionalStringList("item-flags").ifPresent(flags -> applyItemFlags(getItemFlags(flags)));
    }

    private static Material getMaterial(String name) {
        Material material = Material.matchMaterial(name.toUpperCase(Locale.ENGLISH));
        return material != null ? material : Material.BEDROCK;
    }

    public ItemBuilder(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return this;
    }

    public <T, Z> ItemBuilder storePersistentData(NamespacedKey key, PersistentDataType<T,Z> type, Z value) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(key, type, value);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (glow) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer modelData) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(modelData);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setTextureValue(String texture) {
        NMSHandlers.getNmsHandler().setItemStackTexture(this.itemStack, texture);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    @NotNull
    private static Map<Enchantment, Integer> createEnchantmentsMap(@NotNull List<String> enchantments) {
        final Map<Enchantment, Integer> enchantsMap = new HashMap<>(enchantments.size());

        for (String enchantment : enchantments) {
            String[] enchant = enchantment.split(":");
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(enchant[0]));
            if (ench == null) continue;
            int level;
            try {
                level = Integer.parseInt(enchant[1]);
            } catch (Exception exception) {
                continue;
            }

            enchantsMap.put(ench, level);
        }

        return enchantsMap;
    }

    public ItemBuilder applyItemFlags(@NotNull List<ItemFlag> flags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        for (ItemFlag flag : flags) {
            itemMeta.addItemFlags(flag);
        }

        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    @NotNull
    private static List<ItemFlag> getItemFlags(@NotNull List<String> flags) {
        final List<ItemFlag> flagList = new ArrayList<>(flags.size());
        for (String flag : flags) {
            ItemFlag itemFlag;
            try {
                itemFlag = ItemFlag.valueOf(flag.toUpperCase(Locale.ENGLISH));
            } catch (Exception exception) {
                continue;
            }

            flagList.add(itemFlag);
        }

        return flagList;
    }

    public ItemStack get() {
        return itemStack;
    }

    public ItemStack clonedGet() {
        return itemStack.clone();
    }
}
