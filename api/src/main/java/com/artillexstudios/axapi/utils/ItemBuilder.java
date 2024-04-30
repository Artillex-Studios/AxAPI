package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.items.component.DyedColor;
import com.artillexstudios.axapi.items.component.ItemEnchantments;
import com.artillexstudios.axapi.items.component.ItemLore;
import com.artillexstudios.axapi.items.component.ProfileProperties;
import com.artillexstudios.axapi.items.component.Unbreakable;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.google.common.collect.Lists;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ItemBuilder {
    private static final UUID NIL_UUID = new UUID(0, 0);
    private final List<ItemFlag> flags = new ArrayList<>(4);
    private final WrappedItemStack stack;
    private final TagResolver[] resolvers;

    public ItemBuilder(Map<Object, Object> map, TagResolver... resolvers) {
        this.resolvers = resolvers;

        String type = (String) map.get("type");
        if (type == null) {
            type = (String) map.getOrDefault("material", "stone");
        }

        String snbt;
        if ((snbt = (String) map.get("snbt")) != null) {
            stack = NMSHandlers.getNmsHandler().wrapItem(snbt);
        } else {
            stack = WrappedItemStack.wrap(new ItemStack(getMaterial(type)));
        }
        Optional.ofNullable(map.get("item-flags")).ifPresent(flags -> this.flags.addAll(getItemFlags((List<String>) flags)));

        Optional.ofNullable(map.get("name")).ifPresent(name -> this.setName((String) name, resolvers));
        Optional.ofNullable(map.get("color")).ifPresent(color -> this.setColor((String) color));
        Optional.ofNullable(map.get("texture")).ifPresent(string -> this.setTextureValue((String) string));
        Optional.ofNullable(map.get("custom-model-data")).ifPresent(number -> this.setCustomModelData((int) number));
        Optional.ofNullable(map.get("amount")).ifPresent(amount -> stack.setAmount((int) amount));
        Optional.ofNullable(map.get("glow")).ifPresent(glow -> this.glow((boolean) glow));
        Optional.ofNullable(map.get("lore")).ifPresent(lore -> this.setLore((List<String>) lore, resolvers));
        Optional.ofNullable(map.get("enchants")).ifPresent(enchants -> addEnchants(createEnchantmentsMap((List<String>) enchants)));
        Optional.ofNullable(map.get("potion")).ifPresent(potion -> setPotion((String) potion));
        Optional.ofNullable(map.get("unbreakable")).ifPresent(unbreakable -> stack.set(DataComponent.UNBREAKABLE, new Unbreakable(!flags.contains(ItemFlag.HIDE_UNBREAKABLE))));
    }

    public ItemBuilder(Map<Object, Object> map, Map<String, String> replacements) {
        this(map, mapResolvers(replacements));
    }

    public ItemBuilder(Map<Object, Object> map) {
        this(map, TagResolver.resolver());
    }

    public ItemBuilder(Section section) {
        this(mapSection(section), TagResolver.resolver());
    }

    public ItemBuilder(Section section, TagResolver... resolvers) {
        this(mapSection(section), resolvers);
    }

    public ItemBuilder(Section section, Map<String, String> replacements) {
        this(mapSection(section), mapResolvers(replacements));
    }

    public ItemBuilder(ItemStack itemStack, TagResolver... resolvers) {
        this.resolvers = resolvers;
        this.stack = WrappedItemStack.wrap(itemStack);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.resolvers = new TagResolver[]{TagResolver.resolver()};
        this.stack = WrappedItemStack.wrap(itemStack);
    }

    public ItemBuilder(Material material, TagResolver... resolvers) {
        this.resolvers = resolvers;
        this.stack = WrappedItemStack.wrap(new ItemStack(material));
    }

    public ItemBuilder(Material material) {
        this.resolvers = new TagResolver[]{TagResolver.resolver()};
        this.stack = WrappedItemStack.wrap(new ItemStack(material));
    }

    private static Material getMaterial(String name) {
        Material material = Material.matchMaterial(name.toUpperCase(Locale.ENGLISH));
        return material != null ? material : Material.BEDROCK;
    }

    public static TagResolver[] mapResolvers(Map<String, String> replacements) {
        TagResolver[] resolvers = new TagResolver[replacements.size()];

        int i = 0;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            resolvers[i] = Placeholder.parsed(entry.getKey().replace("%", ""), entry.getValue());
            i++;
        }

        return resolvers;
    }

    public static Map<Object, Object> mapSection(Section section) {
        Map<Object, Object> map = new HashMap<>();
        for (Object key : section.getKeys()) {
            map.put(key.toString(), section.get(key.toString()));
        }

        return map;
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


    public ItemBuilder setPotion(String potion) {
        stack.set(DataComponent.POTION_CONTENTS, PotionType.valueOf(potion.toUpperCase(Locale.ENGLISH)));
        return this;
    }

    public ItemBuilder setName(String name) {
        setName(name, resolvers);
        return this;
    }

    public ItemBuilder setName(String name, Map<String, String> replacements) {
        AtomicReference<String> toFormat = new AtomicReference<>(name);
        replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));

        setName(toFormat.get(), TagResolver.resolver());
        return this;
    }

    public ItemBuilder setName(String name, TagResolver... resolvers) {
        stack.set(DataComponent.CUSTOM_NAME, StringUtils.format(toTagResolver(name, resolvers), resolvers));
        return this;
    }

//    public <T, Z> ItemBuilder storePersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
//        meta.getPersistentDataContainer().set(key, type, value);
//        return this;
//    }

    public ItemBuilder setColor(String colorString) {
        String[] rgb = colorString.replace(" ", "").split(",");
        Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

        stack.set(DataComponent.DYED_COLOR, new DyedColor(color, this.flags.contains(ItemFlag.HIDE_DYE)));

        return this;
    }

    public ItemBuilder glow(boolean glow) {
        stack.set(DataComponent.ENCHANTMENT_GLINT_OVERRIDE, glow);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemEnchantments enchants = stack.get(DataComponent.ENCHANTMENTS);
        enchants = enchants.add(enchantment, level);
        if (this.flags.contains(ItemFlag.HIDE_ENCHANTS)) {
            enchants = enchants.withTooltip(false);
        }

        stack.set(DataComponent.ENCHANTMENTS, enchants);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer modelData) {
        stack.set(DataComponent.CUSTOM_MODEL_DATA, modelData);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        setLore(lore, resolvers);
        return this;
    }

    public static String toTagResolver(String string, TagResolver... resolvers) {
        if (!string.contains("%")) {
            return string;
        }

        char[] chars = string.toCharArray();

        int start = -1;

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch != '%') continue;

            if (start == -1) {
                start = i;
            } else {
                StringBuilder builder = new StringBuilder();
                for (int j = start; j <= i; j++) {
                    builder.append(chars[j]);
                }

                if (contains(builder.toString(), resolvers)) {
                    chars[start] = '<';
                    chars[i] = '>';
                }

                start = -1;
            }
        }

        return new String(chars);
    }

    public static boolean contains(String string, TagResolver... resolvers) {
        for (TagResolver resolver : resolvers) {
            if (resolver instanceof TagResolver.Single) {
                TagResolver.Single s = (TagResolver.Single) resolver;
                if (string.contains(s.key())) {
                    System.out.println("CONTAINS! s:" + s.key() + " String:" + string);
                    return true;
                }
            }
        }

        return false;
    }

    public static List<String> toTagResolver(List<String> lore, TagResolver... resolvers) {
        for (int i = 0; i < lore.size(); i++) {
            String toFormat = lore.get(i);
            lore.set(i, toTagResolver(toFormat, resolvers));
        }

        return lore;
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
        stack.set(DataComponent.LORE, new ItemLore(StringUtils.formatList(toTagResolver(lore, resolvers), resolvers)));
        return this;
    }

    public ItemBuilder setTextureValue(String texture) {
        ProfileProperties properties = new ProfileProperties(NIL_UUID, "axapi");
        properties.put("textures", new ProfileProperties.Property("textures", texture, null));
        stack.set(DataComponent.PROFILE, properties);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this::addEnchantment);
        return this;
    }

    public Map<Object, Object> serialize(boolean snbt) {
        HashMap<Object, Object> map = new HashMap<>();
        if (snbt) {
            map.put("snbt", stack.toSNBT());
        } else {
            map.put("type", stack.get(DataComponent.MATERIAL).name());
            map.put("name", MiniMessage.miniMessage().serialize(stack.get(DataComponent.CUSTOM_NAME)));
            map.put("lore", Lists.transform(stack.get(DataComponent.LORE).lines(), a -> MiniMessage.miniMessage().serialize(a)));
            map.put("amount", stack.getAmount());
            map.put("custom-model-data", stack.get(DataComponent.CUSTOM_MODEL_DATA));
            map.put("texture", stack.get(DataComponent.PROFILE).properties().get("textures").stream().findFirst().orElse(new ProfileProperties.Property("", "", null)).value());
        }

        return map;
    }

    public ItemStack get() {
        stack.finishEdit();
        return stack.toBukkit();
    }

    public ItemStack clonedGet() {
        return get().clone();
    }
}
