package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.items.component.type.CustomModelData;
import com.artillexstudios.axapi.items.component.type.DyedColor;
import com.artillexstudios.axapi.items.component.type.ItemEnchantments;
import com.artillexstudios.axapi.items.component.type.ItemLore;
import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.items.component.type.Unbreakable;
import com.artillexstudios.axapi.items.component.type.Unit;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.artillexstudios.axapi.utils.mutable.MutableObject;
import com.google.common.collect.Lists;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
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
import java.util.UUID;
import java.util.function.Consumer;

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
            this.stack = WrappedItemStack.wrap(snbt);
        } else {
            this.stack = WrappedItemStack.wrap(new ItemStack(getMaterial(type)));
        }

        safeMap(map, "item-flags", List.class, flags -> {
            this.flags.addAll(getItemFlags((List<String>) flags));
        });
        safeMap(map, "name", String.class, this::setName);
        safeMap(map, "color", String.class, this::setColor);
        safeMap(map, "texture", String.class, this::setTextureValue);
        safeMap(map, "custom-model-data", Integer.class, this::legacyModelData);
        safeMap(map, "custom-model-data", Map.class, this::customModelData);
        safeMap(map, "amount", Integer.class, this.stack::setAmount);
        safeMap(map, "glow", Boolean.class, this::glow);
        safeMap(map, "lore", List.class, this::setLore);
        safeMap(map, "enchants", List.class, enchants -> {
            this.addEnchants(createEnchantmentsMap((List<String>) enchants));
        });
        safeMap(map, "potion", String.class, this::setPotion);
        safeMap(map, "unbreakable", Object.class, unbreakable -> {
            this.stack.set(DataComponents.unbreakable(), new Unbreakable(!this.flags.contains(ItemFlag.HIDE_UNBREAKABLE)));
        });
        safeMap(map, "item-model", String.class, model -> {
            this.stack.set(DataComponents.itemModel(), Key.key(model));
        });

        try {
            if (this.flags.contains(ItemFlag.HIDE_POTION_EFFECTS)) {
                this.stack.set(DataComponents.hideAdditionalTooltip(), Unit.INSTANCE);
            }
        } catch (Exception ignored) {
        }
    }

    private static <T> void safeMap(Map<Object, Object> map, String key, Class<T> clazz, Consumer<T> consumer) {
        Object o = map.get(key);
        if (o == null) {
            return;
        }

        if (!clazz.isInstance(o)) {
            LogUtils.warn("Failed to safely map {} from {}, because it isn't a {}, but a {}!", key, map, clazz, o.getClass());
            return;
        }

        try {
            consumer.accept(clazz.cast(o));
        } catch (ClassCastException exception) {
            LogUtils.warn("Failed to safely map {} from {}, because it isn't a {}, but a {}!", key, map, clazz, o.getClass());
        }
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
            if (resolver instanceof TagResolver.Single s) {
                if (string.contains(s.key())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static <T> List<String> toTagResolver(List<T> lore, TagResolver... resolvers) {
        List<String> newLore = new ArrayList<>(lore.size());
        for (int i = 0; i < lore.size(); i++) {
            if (!(lore.get(i) instanceof String string)) {
                LogUtils.warn("Failed to turn {} into TagResolvers, because it was not a string, but a {}!", lore.get(i), lore.get(i).getClass());
                continue;
            }

            newLore.add(toTagResolver(string, resolvers));
        }

        return newLore;
    }

    public ItemBuilder setPotion(String potion) {
        stack.set(DataComponents.potionType(), PotionType.valueOf(potion.toUpperCase(Locale.ENGLISH)));
        return this;
    }

//    public <T, Z> ItemBuilder storePersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
//        meta.getPersistentDataContainer().set(key, type, value);
//        return this;
//    }

    public ItemBuilder setName(String name) {
        setName(name, resolvers);
        return this;
    }

    public ItemBuilder setName(String name, Map<String, String> replacements) {
        MutableObject<String> toFormat = new MutableObject<>(name);
        replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));

        setName(toFormat.get(), TagResolver.resolver());
        return this;
    }

    public ItemBuilder setName(String name, TagResolver... resolvers) {
        stack.set(DataComponents.customName(), StringUtils.format(toTagResolver(name, resolvers), resolvers));
        return this;
    }

    public ItemBuilder setColor(String colorString) {
        String[] rgb = colorString.replace(" ", "").split(",");
        Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

        stack.set(DataComponents.dyedColor(), new DyedColor(color, this.flags.contains(ItemFlag.HIDE_DYE)));

        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (glow) {
            stack.set(DataComponents.enchantmentGlintOverride(), true);
        }
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemEnchantments enchants = stack.get(DataComponents.enchantments());
        enchants = enchants.add(enchantment, level);
        if (this.flags.contains(ItemFlag.HIDE_ENCHANTS)) {
            enchants = enchants.withTooltip(false);
        }

        stack.set(DataComponents.enchantments(), enchants);
        return this;
    }

    public ItemBuilder legacyModelData(Integer modelData) {
        stack.set(DataComponents.customModelData(), new CustomModelData(List.of(), List.of(), modelData == null ? List.of() : List.of(modelData.floatValue()), List.of()));
        return this;
    }

    public ItemBuilder customModelData(Map<Object, Object> modelData) {
        stack.set(DataComponents.customModelData(), new CustomModelData((List<String>) modelData.getOrDefault("strings", List.of()), (List<Boolean>) modelData.getOrDefault("flags", List.of()), Lists.transform((List<Number>) modelData.getOrDefault("floats", List.of()), num -> num.floatValue()), Lists.transform((List<String>) modelData.getOrDefault("colors", List.of()), a -> {
            String[] rgb = a.replace(" ", "").split(",");
            Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            return color.asRGB();
        })));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        setLore(lore, resolvers);
        return this;
    }

    public ItemBuilder setLore(List<String> lore, Map<String, String> replacements) {
        List<String> newList = new ArrayList<>(replacements.size());
        for (String line : lore) {
            MutableObject<String> toFormat = new MutableObject<>(line);
            replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));
            newList.add(toFormat.get());
        }

        setLore(newList, TagResolver.resolver());
        return this;
    }

    public ItemBuilder setLore(List<String> lore, TagResolver... resolvers) {
        stack.set(DataComponents.lore(), new ItemLore(StringUtils.formatList(toTagResolver(lore, resolvers), resolvers)));
        return this;
    }

    public ItemBuilder setTextureValue(String texture) {
        ProfileProperties properties = new ProfileProperties(NIL_UUID, "axapi");
        texture = StringUtils.formatToString(toTagResolver(texture), this.resolvers);
        properties.put("textures", new ProfileProperties.Property("textures", texture, null));
        stack.set(DataComponents.profile(), properties);
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

    public WrappedItemStack wrapped() {
        return this.stack;
    }

    public Map<Object, Object> serialize(boolean snbt) {
        HashMap<Object, Object> map = new HashMap<>();
        if (snbt) {
            map.put("snbt", stack.toSNBT());
        } else {
            map.put("type", stack.get(DataComponents.material()).name());

            Component name = stack.get(DataComponents.customName());
            if (name != Component.empty()) {
                map.put("name", MiniMessage.miniMessage().serialize(name));
            }

            List<Component> lore = stack.get(DataComponents.lore()).lines();
            if (!lore.isEmpty()) {
                map.put("lore", Lists.transform(lore, a -> MiniMessage.miniMessage().serialize(a)));
            }

            map.put("amount", stack.getAmount());
            CustomModelData modelData = stack.get(DataComponents.customModelData());
            if (!modelData.floats().isEmpty() && modelData.colors().isEmpty() && modelData.flags().isEmpty() && modelData.strings().isEmpty()) {
                int data = modelData.floats().get(0).intValue();
                if (data != 0) {
                    map.put("custom-model-data", data);
                }
            }

            ProfileProperties profileProperties = stack.get(DataComponents.profile());
            if (profileProperties != null) {
                map.put("texture", profileProperties.properties().get("textures").stream().findFirst().orElse(new ProfileProperties.Property("", "", null)).value());
            }
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
