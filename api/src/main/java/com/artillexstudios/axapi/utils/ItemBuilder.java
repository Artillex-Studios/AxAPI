package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
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

        String snbt;
        if ((snbt = section.getString("snbt")) != null) {
            itemStack = fromSNBT(snbt);
        } else {
            this.itemStack = new ItemStack(getMaterial(type));
        }

        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }
        resolvers = new TagResolver[]{TagResolver.resolver()};

        if (meta == null) return;
        section.getOptionalString("name").ifPresent(name -> this.setName(name, resolvers));
        section.getOptionalString("color").ifPresent(this::setColor);
        if (Version.getServerVersion().isNewerThan(Version.UNKNOWN)) {
            section.getOptionalString("texture").ifPresent(this::setTextureValue);
            section.getOptionalInt("custom-model-data").ifPresent(this::setCustomModelData);
        }
        section.getOptionalInt("amount").ifPresent(this::amount);
        section.getOptionalBoolean("glow").ifPresent(this::glow);
        section.getOptionalStringList("lore").ifPresent(lore -> this.setLore(lore, resolvers));
        Optional.ofNullable(section.get("enchants")).ifPresent((enchants) -> {
            addEnchants(createEnchantmentsMap((List<String>) enchants));
        });
        section.getOptionalStringList("item-flags").ifPresent(flags -> applyItemFlags(getItemFlags(flags)));
        section.getOptionalString("potion").ifPresent(this::setPotion);
    }

    public ItemBuilder(@NotNull Section section, TagResolver... resolvers) {
        String type = section.getString("type");
        if (type == null) {
            type = section.getString("material", "stone");
        }

        String snbt;
        if ((snbt = section.getString("snbt")) != null) {
            itemStack = fromSNBT(snbt);
        } else {
            this.itemStack = new ItemStack(getMaterial(type));
        }

        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }
        this.resolvers = resolvers;

        if (this.meta == null) return;
        section.getOptionalString("name").ifPresent(name -> this.setName(name, resolvers));
        section.getOptionalString("color").ifPresent(this::setColor);
        if (Version.getServerVersion().isNewerThan(Version.UNKNOWN)) {
            section.getOptionalString("texture").ifPresent(this::setTextureValue);
            section.getOptionalInt("custom-model-data").ifPresent(this::setCustomModelData);
        }
        section.getOptionalInt("amount").ifPresent(this::amount);
        section.getOptionalBoolean("glow").ifPresent(this::glow);
        section.getOptionalStringList("lore").ifPresent(lore -> this.setLore(lore, resolvers));
        Optional.ofNullable(section.get("enchants")).ifPresent((enchants) -> {
            addEnchants(createEnchantmentsMap((List<String>) enchants));
        });
        section.getOptionalStringList("item-flags").ifPresent(flags -> applyItemFlags(getItemFlags(flags)));
        section.getOptionalString("potion").ifPresent(this::setPotion);
    }

    public ItemBuilder(Map<Object, Object> map, TagResolver... resolvers) {
        String type = (String) map.get("type");
        if (type == null) {
            type = (String) map.getOrDefault("material", "stone");
        }

        String snbt;
        if ((snbt = (String) map.get("snbt")) != null) {
            itemStack = fromSNBT(snbt);
        } else {
            this.itemStack = new ItemStack(getMaterial(type));
        }

        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }

        this.resolvers = resolvers;

        if (meta == null) return;
        Optional.ofNullable(map.get("name")).ifPresent(name -> this.setName((String) name, resolvers));
        Optional.ofNullable(map.get("color")).ifPresent(color -> this.setColor((String) color));
        if (Version.getServerVersion().isNewerThan(Version.UNKNOWN)) {
            Optional.ofNullable(map.get("texture")).ifPresent(string -> this.setTextureValue((String) string));
            Optional.ofNullable(map.get("custom-model-data")).ifPresent(number -> this.setCustomModelData((int) number));
        }
        Optional.ofNullable(map.get("amount")).ifPresent(amount -> itemStack.setAmount((int) amount));
        Optional.ofNullable(map.get("glow")).ifPresent(glow -> this.glow((boolean) glow));
        Optional.ofNullable(map.get("lore")).ifPresent(lore -> this.setLore((List<String>) lore, resolvers));
        Optional.ofNullable(map.get("enchants")).ifPresent(enchants -> addEnchants(createEnchantmentsMap((List<String>) enchants)));
        Optional.ofNullable(map.get("item-flags")).ifPresent(flags -> applyItemFlags(getItemFlags((List<String>) flags)));
        Optional.ofNullable(map.get("potion")).ifPresent(potion -> setPotion((String) potion));
    }

    public ItemBuilder(@NotNull Section section, Map<String, String> replacements) {
        String type = section.getString("type");
        if (type == null) {
            type = section.getString("material", "stone");
        }

        String snbt;
        if ((snbt = section.getString("snbt")) != null) {
            itemStack = fromSNBT(snbt);
        } else {
            this.itemStack = new ItemStack(getMaterial(type));
        }

        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }
        this.resolvers = new TagResolver[]{TagResolver.resolver()};

        if (this.meta == null) return;
        section.getOptionalString("name").ifPresent(name -> this.setName(name, replacements));
        section.getOptionalString("color").ifPresent(this::setColor);
        if (Version.getServerVersion().isNewerThan(Version.UNKNOWN)) {
            section.getOptionalString("texture").ifPresent(this::setTextureValue);
            section.getOptionalInt("custom-model-data").ifPresent(this::setCustomModelData);
        }
        section.getOptionalInt("amount").ifPresent(this::amount);
        section.getOptionalBoolean("glow").ifPresent(this::glow);
        section.getOptionalStringList("lore").ifPresent(lore -> this.setLore(lore, replacements));
        Optional.ofNullable(section.get("enchants")).ifPresent((enchants) -> {
            addEnchants(createEnchantmentsMap((List<String>) enchants));
        });
        section.getOptionalStringList("item-flags").ifPresent(flags -> applyItemFlags(getItemFlags(flags)));
        section.getOptionalString("potion").ifPresent(this::setPotion);
    }

    public ItemBuilder(Map<Object, Object> map, Map<String, String> replacements) {
        String type = (String) map.get("type");
        if (type == null) {
            type = (String) map.getOrDefault("material", "stone");
        }

        String snbt;
        if ((snbt = (String) map.get("snbt")) != null) {
            itemStack = fromSNBT(snbt);
        } else {
            this.itemStack = new ItemStack(getMaterial(type));
        }
        if (!itemStack.getType().isAir()) {
            this.meta = itemStack.getItemMeta();
        } else {
            this.meta = null;
        }

        this.resolvers = new TagResolver[]{TagResolver.resolver()};
        ;

        if (meta == null) return;
        Optional.ofNullable(map.get("name")).ifPresent(name -> this.setName((String) name, replacements));
        Optional.ofNullable(map.get("color")).ifPresent(color -> this.setColor((String) color));
        if (Version.getServerVersion().isNewerThan(Version.UNKNOWN)) {
            Optional.ofNullable(map.get("texture")).ifPresent(string -> this.setTextureValue((String) string));
            Optional.ofNullable(map.get("custom-model-data")).ifPresent(number -> this.setCustomModelData((int) number));
        }
        Optional.ofNullable(map.get("amount")).ifPresent(amount -> itemStack.setAmount((int) amount));
        Optional.ofNullable(map.get("glow")).ifPresent(glow -> this.glow((boolean) glow));
        Optional.ofNullable(map.get("lore")).ifPresent(lore -> this.setLore((List<String>) lore, replacements));
        Optional.ofNullable(map.get("enchants")).ifPresent(enchants -> addEnchants(createEnchantmentsMap((List<String>) enchants)));
        Optional.ofNullable(map.get("item-flags")).ifPresent(flags -> applyItemFlags(getItemFlags((List<String>) flags)));
        Optional.ofNullable(map.get("potion")).ifPresent(potion -> setPotion((String) potion));
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

    private static Material getMaterial(String name) {
        Material material = Material.matchMaterial(name.toUpperCase(Locale.ENGLISH));
        return material != null ? material : Material.BEDROCK;
    }

    @NotNull
    private static Map<Enchantment, Integer> createEnchantmentsMap(@NotNull List<String> enchantments) {
        final Map<Enchantment, Integer> enchantsMap = new HashMap<>(enchantments.size());

        for (String enchantment : enchantments) {
            String[] enchant = enchantment.split(":");
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(enchant[0]));


            if (ench == null) {
                continue;
            }
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

    public static ItemStack fromSNBT(String snbt) {
        return NMSHandlers.getNmsHandler().fromSNBT(snbt);
    }

    public ItemBuilder setPotion(String potion) {
        if (meta instanceof PotionMeta) {
            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(potion.toUpperCase(Locale.ENGLISH))));
        }

        return this;
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

    public <T, Z> ItemBuilder storePersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemBuilder setColor(String colorString) {
        String[] rgb = colorString.replace(" ", "").split(",");
        Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta itemMeta = (LeatherArmorMeta) meta;
            itemMeta.setColor(color);
        } else if (meta instanceof PotionMeta) {
            PotionMeta itemMeta = (PotionMeta) meta;
            itemMeta.setColor(color);
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
        NMSHandlers.getNmsHandler().setItemStackTexture(this.meta, texture);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach((enchant, level) -> {
            meta.addEnchant(enchant, level, true);
        });
        return this;
    }

    public ItemBuilder applyItemFlags(@NotNull List<ItemFlag> flags) {
        for (ItemFlag flag : flags) {
            meta.addItemFlags(flag);
        }

        return this;
    }

    public String toSNBT() {
        return NMSHandlers.getNmsHandler().toSNBT(get());
    }

    public Map<Object, Object> toMap(boolean snbt) {
        HashMap<Object, Object> map = new HashMap<>();
        if (snbt) {
            map.put("snbt", toSNBT());
        } else {
            map.put("type", itemStack.getType().name());
            map.put("name", meta.getDisplayName());
            map.put("lore", meta.getLore());

            map.put("amount", itemStack.getAmount());
            if (meta.hasCustomModelData()) {
                map.put("custom-model-data", meta.getCustomModelData());
            }

            String texture;
            if ((texture = NMSHandlers.getNmsHandler().getTextureValue(meta)) != null) {
                map.put("texture", texture);
            }
        }

        return map;
    }

    public ItemStack get() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack clonedGet() {
        return get().clone();
    }
}
