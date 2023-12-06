package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta meta;
    private final TagResolver[] resolvers;

    public ItemBuilder(@NotNull Section section) {
        String type = section.getString("type");
        if (type == null) {
            type = section.getString("material", "stone");
        }
        
        this.itemStack = new ItemStack(getMaterial(type));
        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }
        resolvers = new TagResolver[]{TagResolver.resolver()};

        if (meta == null) return;
        section.getOptionalString("texture").ifPresent(this::setTextureValue);
        section.getOptionalString("name").ifPresent(name -> this.setName(name, resolvers));
        section.getOptionalString("color").ifPresent(this::setColor);
        section.getOptionalInt("custom-model-data").ifPresent(this::setCustomModelData);
        section.getOptionalInt("amount").ifPresent(this::amount);
        section.getOptionalBoolean("glow").ifPresent(this::glow);
        section.getOptionalStringList("lore").ifPresent(lore -> this.setLore(lore, resolvers));
        section.getOptionalStringList("enchants").ifPresent(enchants -> addEnchants(createEnchantmentsMap(enchants)));
        section.getOptionalStringList("item-flags").ifPresent(flags -> applyItemFlags(getItemFlags(flags)));
    }

    public ItemBuilder(@NotNull Section section, TagResolver... resolvers) {
        String type = section.getString("type");
        if (type == null) {
            type = section.getString("material", "stone");
        }

        this.itemStack = new ItemStack(getMaterial(type));
        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }
        this.resolvers = resolvers;

        if (this.meta == null) return;
        section.getOptionalString("texture").ifPresent(this::setTextureValue);
        section.getOptionalString("name").ifPresent(name -> this.setName(name, resolvers));
        section.getOptionalString("color").ifPresent(this::setColor);
        section.getOptionalInt("custom-model-data").ifPresent(this::setCustomModelData);            
        section.getOptionalInt("amount").ifPresent(this::amount);
        section.getOptionalBoolean("glow").ifPresent(this::glow);
        section.getOptionalStringList("lore").ifPresent(lore -> this.setLore(lore, resolvers));
        section.getOptionalStringList("enchants").ifPresent(enchants -> addEnchants(createEnchantmentsMap(enchants)));
        section.getOptionalStringList("item-flags").ifPresent(flags -> applyItemFlags(getItemFlags(flags)));
    }

    public ItemBuilder(Map<Object, Object> map, TagResolver... resolvers) {
        String type = (String) map.get("type");
        if (type == null) {
            type = (String) map.getOrDefault("material", "stone");
        }

        this.itemStack = new ItemStack(getMaterial(type));
        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }

        this.resolvers = resolvers;

        if (meta == null) return;
        Optional.ofNullable(map.get("texture")).ifPresent(string -> this.setTextureValue((String) string));
        Optional.ofNullable(map.get("name")).ifPresent(name -> this.setName((String) name, resolvers));
        Optional.ofNullable(map.get("color")).ifPresent(color -> this.setColor((String) color));
        Optional.ofNullable(map.get("custom-model-data")).ifPresent(number -> this.setCustomModelData((int) number));
        Optional.ofNullable(map.get("amount")).ifPresent(amount -> itemStack.setAmount((int) amount));
        Optional.ofNullable(map.get("glow")).ifPresent(glow -> this.glow((boolean) glow));
        Optional.ofNullable(map.get("lore")).ifPresent(lore -> this.setLore((List<String>) lore, resolvers));
        Optional.ofNullable(map.get("enchants")).ifPresent(enchants -> addEnchants(createEnchantmentsMap((List<String>) enchants)));
        Optional.ofNullable(map.get("item-flags")).ifPresent(flags -> applyItemFlags(getItemFlags((List<String>) flags)));
    }

    private static Material getMaterial(String name) {
        Material material = Material.matchMaterial(name.toUpperCase(Locale.ENGLISH));
        return material != null ? material : Material.BEDROCK;
    }

    public ItemBuilder(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }
        resolvers = new TagResolver[]{TagResolver.resolver()};
    }

    public ItemBuilder(@NotNull ItemStack item) {
        this.itemStack = item;
        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }
        resolvers = new TagResolver[]{TagResolver.resolver()};
    }

    public ItemBuilder setName(String name) {
        setName(name, TagResolver.resolver());
        return this;
    }

    public ItemBuilder setName(String name, Map<String, String> replacements) {
        AtomicReference<String> toFormat = new AtomicReference<>(name);
        replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));

        setName(toFormat.get(), TagResolver.resolver());
        return this;
    }

    public ItemBuilder setName(String name, TagResolver... resolvers) {
        meta.setDisplayName(StringUtils.formatToString(name, resolvers));
        return this;
    }

    public <T, Z> ItemBuilder storePersistentData(NamespacedKey key, PersistentDataType<T,Z> type, Z value) {
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemBuilder setColor(String colorString) {
        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta itemMeta = (LeatherArmorMeta) meta;
            String[] rgb = colorString.replace(" ", "").split(",");
            itemMeta.setColor(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
        }
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (glow) {
            meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer modelData) {
        meta.setCustomModelData(modelData);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        setLore(lore, TagResolver.resolver());
        return this;
    }

    public ItemBuilder setLore(List<String> lore, Map<String, String> replacements) {
        List<String> newList = new ArrayList<>(replacements.size());
        for (String line : lore) {
            AtomicReference<String> toFormat = new AtomicReference<>(line);
            replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));
            newList.add(toFormat.get());
        }

        setLore(newList, TagResolver.resolver());
        return this;
    }

    public ItemBuilder setLore(List<String> lore, TagResolver... resolvers) {
        meta.setLore(StringUtils.formatListToString(lore, resolvers));
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
        for (ItemFlag flag : flags) {
            meta.addItemFlags(flag);
        }

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
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack clonedGet() {
        return get().clone();
    }
}
