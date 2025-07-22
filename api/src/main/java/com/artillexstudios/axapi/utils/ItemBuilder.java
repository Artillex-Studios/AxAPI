package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.config.adapters.ConfigurationGetter;
import com.artillexstudios.axapi.config.adapters.MapConfigurationGetter;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.items.component.type.CustomModelData;
import com.artillexstudios.axapi.items.component.type.DyedColor;
import com.artillexstudios.axapi.items.component.type.ItemEnchantments;
import com.artillexstudios.axapi.items.component.type.ItemLore;
import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.items.component.type.Unbreakable;
import com.artillexstudios.axapi.items.component.type.Unit;
import com.artillexstudios.axapi.placeholders.PlaceholderParameters;
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
import java.util.Set;
import java.util.UUID;

public class ItemBuilder {
    private static final UUID NIL_UUID = new UUID(0, 0);
    private final List<ItemFlag> flags = new ArrayList<>(4);
    private final WrappedItemStack stack;
    private final TagResolver[] resolvers;
    private final PlaceholderParameters parameters;

    public ItemBuilder(ConfigurationGetter getter, PlaceholderParameters params, TagResolver[] resolvers) {
        this.resolvers = resolvers;
        this.parameters = params;

        String snbt = getter.getString("snbt");
        if (snbt != null) {
            this.stack = WrappedItemStack.wrap(snbt);
        } else {
            String type = getter.anyOf(getter::getString, "material", "type");
            // TODO: Maybe default item
            this.stack = WrappedItemStack.wrap(new ItemStack(this.getMaterial(type)));
        }

        Optionals.ifPresent(getter.getStringList("item-flags"), list -> this.flags.addAll(this.getItemFlags(list)));
        Optionals.ifPresent(getter.getStringList("enchants"), enchants -> this.addEnchants(this.createEnchantmentsMap(enchants)));
        Optionals.ifPresent(getter.getString("name"), this::setName);
        Optionals.ifPresent(getter.getString("color"), this::setColor);
        Optionals.ifPresent(getter.getString("texture"), this::setTextureValue);
        Optionals.ifPresent(getter.getInteger("amount"), this::amount);
        Optionals.ifPresent(getter.getBoolean("glow"), this::glow);
        Optionals.ifPresent(getter.getStringList("lore"), this::setLore);
        Optionals.ifPresent(getter.getString("potion"), this::setPotion);
        Optionals.ifPresent(getter.getString("item-model"), this::setPotion);
        Optionals.ifPresent(getter.getBoolean("unbreakable"), this::unbreakable);
        Optionals.ifPresent(getter.getString("item-model"), this::itemModel);
        Optionals.ifPresent(ExceptionUtils.catching(() -> getter.getInteger("custom-model-data")), this::legacyModelData);
        Optionals.ifPresent(ExceptionUtils.catching(() -> getter.getMap("custom-model-data")), this::customModelData);

        ExceptionUtils.catching(() -> {
            if (this.flags.contains(ItemFlag.HIDE_POTION_EFFECTS)) {
                this.stack.set(DataComponents.hideAdditionalTooltip(), Unit.INSTANCE);
            }
        });
    }

    public ItemBuilder(WrappedItemStack stack, PlaceholderParameters parameters, TagResolver... resolvers) {
        this.stack = stack;
        this.parameters = parameters;
        this.resolvers = resolvers;
    }

    public static ItemBuilder create(WrappedItemStack stack) {
        return create(stack, (PlaceholderParameters) null);
    }

    public static ItemBuilder create(WrappedItemStack stack, TagResolver... resolvers) {
        return create(stack, null, resolvers);
    }

    public static ItemBuilder create(WrappedItemStack stack, Map<String, String> replacements) {
        return create(stack, null, replacements);
    }

    public static ItemBuilder create(WrappedItemStack stack, PlaceholderParameters parameters) {
        return create(stack, parameters, TagResolver.resolver());
    }

    public static ItemBuilder create(WrappedItemStack stack, PlaceholderParameters parameters, Map<String, String> replacements) {
        return create(stack, parameters, mapResolvers(replacements));
    }

    public static ItemBuilder create(WrappedItemStack stack, PlaceholderParameters parameters, TagResolver... resolvers) {
        return new ItemBuilder(stack, parameters, resolvers);
    }

    public static ItemBuilder create(ItemStack stack) {
        return create(stack, (PlaceholderParameters) null);
    }

    public static ItemBuilder create(ItemStack stack, TagResolver... resolvers) {
        return create(stack, null, resolvers);
    }

    public static ItemBuilder create(ItemStack stack, Map<String, String> replacements) {
        return create(stack, null, replacements);
    }

    public static ItemBuilder create(ItemStack stack, PlaceholderParameters parameters) {
        return create(stack, parameters, TagResolver.resolver());
    }

    public static ItemBuilder create(ItemStack stack, PlaceholderParameters parameters, Map<String, String> replacements) {
        return create(stack, parameters, mapResolvers(replacements));
    }

    public static ItemBuilder create(ItemStack stack, PlaceholderParameters parameters, TagResolver... resolvers) {
        return create(WrappedItemStack.wrap(stack), parameters, resolvers);
    }

    public static ItemBuilder create(Material material) {
        return create(material, (PlaceholderParameters) null);
    }

    public static ItemBuilder create(Material material, TagResolver... resolvers) {
        return create(material, null, resolvers);
    }

    public static ItemBuilder create(Material material, Map<String, String> replacements) {
        return create(material, null, replacements);
    }

    public static ItemBuilder create(Material material, PlaceholderParameters parameters) {
        return create(material, parameters, TagResolver.resolver());
    }

    public static ItemBuilder create(Material material, PlaceholderParameters parameters, Map<String, String> replacements) {
        return create(material, parameters, mapResolvers(replacements));
    }

    public static ItemBuilder create(Material stack, PlaceholderParameters parameters, TagResolver... resolvers) {
        return create(new ItemStack(stack), parameters, resolvers);
    }

    public static ItemBuilder create(Section section) {
        return create(section, (PlaceholderParameters) null);
    }

    public static ItemBuilder create(Section section, TagResolver... resolvers) {
        return create(section, null, resolvers);
    }

    public static ItemBuilder create(Section section, Map<String, String> replacements) {
        return create(section, null, replacements);
    }

    public static ItemBuilder create(Section section, PlaceholderParameters parameters) {
        return create(section, parameters, TagResolver.resolver());
    }

    public static ItemBuilder create(Section section, PlaceholderParameters parameters, Map<String, String> replacements) {
        return create(section, parameters, mapResolvers(replacements));
    }

    public static ItemBuilder create(Section section, PlaceholderParameters parameters, TagResolver... resolvers) {
        return create(mapSection(section), parameters, resolvers);
    }

    public static <T, Z> ItemBuilder create(Map<T, Z> section) {
        return create(section, (PlaceholderParameters) null);
    }

    public static <T, Z> ItemBuilder create(Map<T, Z> section, Map<String, String> replacements) {
        return create(section, null, replacements);
    }

    public static <T, Z> ItemBuilder create(Map<T, Z> section, TagResolver... resolvers) {
        return create(section, null, resolvers);
    }

    public static <T, Z> ItemBuilder create(Map<T, Z> section, PlaceholderParameters parameters) {
        return create(section, parameters, TagResolver.resolver());
    }

    public static <T, Z> ItemBuilder create(Map<T, Z> section, PlaceholderParameters parameters, Map<String, String> replacements) {
        return create(section, parameters, mapResolvers(replacements));
    }

    public static <T, Z> ItemBuilder create(Map<T, Z> section, PlaceholderParameters parameters, TagResolver... resolvers) {
        return create(new MapConfigurationGetter(section), parameters, resolvers);
    }

    public static ItemBuilder create(ConfigurationGetter getter) {
        return create(getter, (PlaceholderParameters) null);
    }

    public static ItemBuilder create(ConfigurationGetter getter, TagResolver... resolvers) {
        return create(getter, null, resolvers);
    }

    public static ItemBuilder create(ConfigurationGetter getter, Map<String, String> replacements) {
        return create(getter, null, replacements);
    }

    public static ItemBuilder create(ConfigurationGetter getter, PlaceholderParameters parameters) {
        return create(getter, parameters, TagResolver.resolver());
    }

    public static ItemBuilder create(ConfigurationGetter getter, PlaceholderParameters parameters, Map<String, String> replacements) {
        return create(getter, parameters, mapResolvers(replacements));
    }

    public static ItemBuilder create(ConfigurationGetter getter, PlaceholderParameters parameters, TagResolver... resolvers) {
        return new ItemBuilder(getter, parameters, resolvers);
    }

    private Material getMaterial(String name) {
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

    private static Map<Object, Object> mapSection(Section section) {
        Set<Object> keys = section.getKeys();
        Map<Object, Object> map = new HashMap<>(keys.size());
        for (Object key : keys) {
            map.put(key.toString(), section.get(key.toString()));
        }

        return map;
    }

    @NotNull
    private Map<Enchantment, Integer> createEnchantmentsMap(@NotNull List<String> enchantments) {
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
    private List<ItemFlag> getItemFlags(@NotNull List<String> flags) {
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
        this.stack.set(DataComponents.potionType(), PotionType.valueOf(potion.toUpperCase(Locale.ENGLISH)));
        return this;
    }

//    public <T, Z> ItemBuilder storePersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
//        meta.getPersistentDataContainer().set(key, type, value);
//        return this;
//    }

    public ItemBuilder setName(String name) {
        setName(name, this.resolvers);
        return this;
    }

    public ItemBuilder setName(String name, Map<String, String> replacements) {
        MutableObject<String> toFormat = new MutableObject<>(name);
        replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));

        setName(toFormat.get(), TagResolver.resolver());
        return this;
    }

    public ItemBuilder setName(String name, TagResolver... resolvers) {
        this.stack.set(DataComponents.customName(), StringUtils.format(toTagResolver(name, resolvers), resolvers));
        return this;
    }

    public ItemBuilder setColor(String colorString) {
        String[] rgb = colorString.replace(" ", "").split(",");
        Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

        this.stack.set(DataComponents.dyedColor(), new DyedColor(color, this.flags.contains(ItemFlag.HIDE_DYE)));

        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (glow) {
            this.stack.set(DataComponents.enchantmentGlintOverride(), true);
        }
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        if (unbreakable) {
            this.stack.set(DataComponents.unbreakable(), new Unbreakable(!this.flags.contains(ItemFlag.HIDE_UNBREAKABLE)));
        } else {
            this.stack.set(DataComponents.unbreakable(), null);
        }
        return this;
    }

    public ItemBuilder itemModel(String model) {
        this.stack.set(DataComponents.itemModel(), model == null ? null : Key.key(model));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemEnchantments enchants = this.stack.get(DataComponents.enchantments());
        enchants = enchants.add(enchantment, level);
        if (this.flags.contains(ItemFlag.HIDE_ENCHANTS)) {
            enchants = enchants.withTooltip(false);
        }

        this.stack.set(DataComponents.enchantments(), enchants);
        return this;
    }

    public ItemBuilder legacyModelData(Integer modelData) {
        this.stack.set(DataComponents.customModelData(), new CustomModelData(List.of(), List.of(), modelData == null ? List.of() : List.of(modelData.floatValue()), List.of()));
        return this;
    }

    public ItemBuilder customModelData(Map<Object, Object> modelData) {
        this.stack.set(DataComponents.customModelData(), new CustomModelData((List<String>) modelData.getOrDefault("strings", List.of()), (List<Boolean>) modelData.getOrDefault("flags", List.of()), Lists.transform((List<Number>) modelData.getOrDefault("floats", List.of()), num -> num.floatValue()), Lists.transform((List<String>) modelData.getOrDefault("colors", List.of()), a -> {
            String[] rgb = a.replace(" ", "").split(",");
            Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            return color.asRGB();
        })));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        setLore(lore, this.resolvers);
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
        this.stack.set(DataComponents.lore(), new ItemLore(StringUtils.formatList(toTagResolver(lore, resolvers), resolvers)));
        return this;
    }

    public ItemBuilder setTextureValue(String texture) {
        ProfileProperties properties = new ProfileProperties(NIL_UUID, "axapi");
        texture = StringUtils.formatToString(toTagResolver(texture), this.resolvers);
        properties.put("textures", new ProfileProperties.Property("textures", texture, null));
        this.stack.set(DataComponents.profile(), properties);
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
            map.put("snbt", this.stack.toSNBT());
        } else {
            map.put("type", this.stack.get(DataComponents.material()).name());

            Component name = this.stack.get(DataComponents.customName());
            if (name != Component.empty()) {
                map.put("name", MiniMessage.miniMessage().serialize(name));
            }

            List<Component> lore = this.stack.get(DataComponents.lore()).lines();
            if (!lore.isEmpty()) {
                map.put("lore", Lists.transform(lore, a -> MiniMessage.miniMessage().serialize(a)));
            }

            map.put("amount", this.stack.getAmount());
            CustomModelData modelData = this.stack.get(DataComponents.customModelData());
            if (!modelData.floats().isEmpty() && modelData.colors().isEmpty() && modelData.flags().isEmpty() && modelData.strings().isEmpty()) {
                int data = modelData.floats().get(0).intValue();
                if (data != 0) {
                    map.put("custom-model-data", data);
                }
            }

            ProfileProperties profileProperties = this.stack.get(DataComponents.profile());
            if (profileProperties != null) {
                map.put("texture", profileProperties.properties().get("textures").stream().findFirst().orElse(new ProfileProperties.Property("", "", null)).value());
            }
        }

        return map;
    }

    public ItemStack get() {
        this.stack.finishEdit();
        return this.stack.toBukkit();
    }

    public ItemStack clonedGet() {
        return this.get().clone();
    }
}
