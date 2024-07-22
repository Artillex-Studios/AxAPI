package com.artillexstudios.axapi.nms.v1_19_R3.items.data;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.items.component.DyedColor;
import com.artillexstudios.axapi.items.component.ItemEnchantments;
import com.artillexstudios.axapi.items.component.ItemLore;
import com.artillexstudios.axapi.items.component.ProfileProperties;
import com.artillexstudios.axapi.items.component.Rarity;
import com.artillexstudios.axapi.items.component.Unbreakable;
import com.artillexstudios.axapi.items.component.Unit;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_19_R3.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataComponentImpl implements com.artillexstudios.axapi.items.component.DataComponentImpl {

    private static void addItemFlags(net.minecraft.nbt.CompoundTag tag, ItemFlag... itemFlags) {
        byte flag = tag.contains("HideFlags", 99) ? (byte) tag.getInt("HideFlags") : (byte) 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag |= getBitModifier(itemFlag);
        }

        tag.putInt("HideFlags", flag);
    }

    private static byte getBitModifier(ItemFlag hideFlag) {
        return (byte) (1 << hideFlag.ordinal());
    }

    private static void setDisplayTag(net.minecraft.nbt.CompoundTag compoundTag, String key, @Nullable Tag value) {
        final net.minecraft.nbt.CompoundTag display = compoundTag.getCompound(DISPLAY_TAG);

        if (!compoundTag.contains(DISPLAY_TAG)) {
            compoundTag.put(DISPLAY_TAG, display);
        }

        display.remove(key);

        if (value != null) {
            display.put(key, value);
        }
    }

    private static void removeItemFlags(net.minecraft.nbt.CompoundTag tag, ItemFlag... itemFlags) {
        byte flag = tag.contains("HideFlags", 99) ? (byte) tag.getInt("HideFlags") : 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag &= (byte) ~(byte) getBitModifier(itemFlag);
        }

        tag.putInt("HideFlags", flag);
    }

    private static boolean hasItemFlag(net.minecraft.nbt.CompoundTag tag, ItemFlag itemFlag) {
        byte flag = tag.contains("HideFlags", 99) ? (byte) tag.getInt("HideFlags") : 0;
        int bitModifier = getBitModifier(itemFlag);
        return (flag & bitModifier) == bitModifier;
    }

    private static net.minecraft.nbt.CompoundTag getDisplayTag(net.minecraft.nbt.CompoundTag compoundTag) {
        if (!compoundTag.contains(DISPLAY_TAG)) {
            return null;
        }

        return compoundTag.getCompound(DISPLAY_TAG);
    }

    private static Enchantment minecraftToBukkit(net.minecraft.world.item.enchantment.Enchantment minecraft) {
        Preconditions.checkArgument(minecraft != null);
        Registry<net.minecraft.world.item.enchantment.Enchantment> registry = BuiltInRegistries.ENCHANTMENT;
        Enchantment bukkit = org.bukkit.Registry.ENCHANTMENT.get(CraftNamespacedKey.fromMinecraft((registry.getResourceKey(minecraft).orElseThrow()).location()));
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }

    @Override
    public DataComponent<CompoundTag> customData() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, CompoundTag compoundTag) {
                ItemStack itemStack = (ItemStack) item;
                itemStack.setTag((net.minecraft.nbt.CompoundTag) compoundTag.getParent());
            }

            @Override
            public CompoundTag get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                return new com.artillexstudios.axapi.nms.v1_19_R3.items.nbt.CompoundTag(tag == null ? new net.minecraft.nbt.CompoundTag() : tag);
            }
        };
    }

    @Override
    public DataComponent<Integer> maxStackSize() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {

            }

            @Override
            public Integer get(Object item) {
                return 0;
            }
        };
    }

    @Override
    public DataComponent<Integer> maxDamage() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {
            }

            @Override
            public Integer get(Object item) {
                return 0;
            }
        };
    }

    @Override
    public DataComponent<Integer> damage() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {
                ItemStack itemStack = (ItemStack) item;
                if (integer == null || integer == 0) {
                    itemStack.setDamageValue(0);
                    return;
                }

                itemStack.setDamageValue(integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return itemStack.getDamageValue();
            }
        };
    }

    @Override
    public DataComponent<Unbreakable> unbreakable() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unbreakable unbreakable) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (unbreakable == null) {
                    tag.remove("Unbreakable");
                    return;
                }

                if (!unbreakable.showInTooltip()) {
                    addItemFlags(tag, ItemFlag.HIDE_UNBREAKABLE);
                }

                tag.putBoolean("Unbreakable", true);
            }

            @Override
            public Unbreakable get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                if (tag == null || !tag.contains("Unbreakable")) {
                    return null;
                }

                return new Unbreakable(!hasItemFlag(tag, ItemFlag.HIDE_UNBREAKABLE));
            }
        };
    }

    @Override
    public DataComponent<Component> customName() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Component component) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (component == null) {
                    setDisplayTag(tag, "Name", null);
                    return;
                }

                setDisplayTag(tag, "Name", StringTag.valueOf(ComponentSerializer.INSTANCE.toGson(component)));
            }

            @Override
            public Component get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                if (tag == null) {
                    return Component.empty();
                }

                net.minecraft.nbt.CompoundTag display = getDisplayTag(tag);
                if (display == null) {
                    return Component.empty();
                }

                if (!display.contains("Name")) {
                    return Component.empty();
                }

                return ComponentSerializer.INSTANCE.fromGson(display.getString("Name"));
            }
        };
    }

    @Override
    public DataComponent<Component> itemName() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Component component) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (component == null) {
                    setDisplayTag(tag, "Name", null);
                    return;
                }

                setDisplayTag(tag, "Name", StringTag.valueOf(ComponentSerializer.INSTANCE.toGson(component)));
            }

            @Override
            public Component get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                if (tag == null) {
                    return Component.empty();
                }

                net.minecraft.nbt.CompoundTag display = getDisplayTag(tag);
                if (display == null) {
                    return Component.empty();
                }

                if (!display.contains("Name")) {
                    return Component.empty();
                }

                return ComponentSerializer.INSTANCE.fromGson(display.getString("Name"));
            }
        };
    }

    @Override
    public DataComponent<ItemLore> lore() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ItemLore itemLore) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (itemLore == null) {
                    setDisplayTag(tag, "Lore", null);
                    return;
                }

                ListTag listTag = new ListTag();
                List<String> jsonLore = ComponentSerializer.INSTANCE.toGsonList(itemLore.lines());

                for (int i = 0; i < jsonLore.size(); i++) {
                    listTag.add(StringTag.valueOf(jsonLore.get(i)));
                }

                setDisplayTag(tag, "Lore", listTag);
            }

            @Override
            public ItemLore get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                if (tag == null) {
                    return new ItemLore(List.of(), List.of());
                }

                net.minecraft.nbt.CompoundTag display = getDisplayTag(tag);
                if (display == null) {
                    return new ItemLore(List.of(), List.of());
                }

                if (!display.contains("Lore")) {
                    return new ItemLore(List.of(), List.of());
                }

                ListTag list = display.getList("Lore", CraftMagicNumbers.NBT.TAG_STRING);
                ArrayList<String> lore = new ArrayList<>(list.size());
                for (int index = 0; index < list.size(); index++) {
                    String line = list.getString(index);
                    lore.add(line);
                }

                return new ItemLore(ComponentSerializer.INSTANCE.fromGsonList(lore));
            }
        };
    }

    @Override
    public DataComponent<Rarity> rarity() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Rarity rarity) {

            }

            @Override
            public Rarity get(Object item) {
                return Rarity.COMMON;
            }
        };
    }

    @Override
    public DataComponent<ItemEnchantments> enchantments() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ItemEnchantments itemEnchantments) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (itemEnchantments == null) {
                    tag.remove("Enchantments");
                    return;
                }

                HashMap<net.minecraft.world.item.enchantment.Enchantment, Integer> enchantments = new HashMap<>();
                for (Map.Entry<Enchantment, Integer> entry : itemEnchantments.entrySet()) {
                    enchantments.put(CraftEnchantment.getRaw(entry.getKey()), entry.getValue());
                }

                EnchantmentHelper.setEnchantments(enchantments, itemStack);
                if (!itemEnchantments.showInTooltip()) {
                    addItemFlags(tag, ItemFlag.HIDE_ENCHANTS);
                }
            }

            @Override
            public ItemEnchantments get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();

                Map<net.minecraft.world.item.enchantment.Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(itemStack);
                HashMap<Enchantment, Integer> enchantments = new HashMap<>();
                for (Map.Entry<net.minecraft.world.item.enchantment.Enchantment, Integer> entry : enchants.entrySet()) {
                    enchantments.put(minecraftToBukkit(entry.getKey()), entry.getValue());
                }

                return new ItemEnchantments(enchantments, tag == null ? true : !hasItemFlag(tag, ItemFlag.HIDE_ENCHANTS));
            }
        };
    }

    @Override
    public DataComponent<Integer> customModelData() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (integer == null || integer == 0) {
                    tag.remove("CustomModelData");
                    return;
                }

                tag.putInt("CustomModelData", integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                return tag == null ? 0 : tag.getInt("CustomModelData");
            }
        };
    }

    @Override
    public DataComponent<Unit> hideAdditionalTooltip() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (unit == null) {
                    removeItemFlags(tag, ItemFlag.HIDE_ITEM_SPECIFICS);
                    return;
                }

                addItemFlags(tag, ItemFlag.HIDE_ITEM_SPECIFICS);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                return tag == null ? null : hasItemFlag(tag, ItemFlag.HIDE_ITEM_SPECIFICS) ? Unit.INSTANCE : null;
            }
        };
    }

    @Override
    public DataComponent<Unit> hideTooltip() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (unit == null) {
                    removeItemFlags(tag, ItemFlag.HIDE_ENCHANTS);
                    return;
                }

                addItemFlags(tag, ItemFlag.HIDE_ENCHANTS);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                return tag == null ? null : hasItemFlag(tag, ItemFlag.HIDE_ENCHANTS) ? Unit.INSTANCE : null;
            }
        };
    }

    @Override
    public DataComponent<Integer> repairCost() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {
                ItemStack itemStack = (ItemStack) item;
                if (integer == null) {
                    itemStack.setRepairCost(0);
                    return;
                }

                itemStack.setRepairCost(integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.getBaseRepairCost();
            }
        };
    }

    @Override
    public DataComponent<Unit> creativeSlotLock() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {

            }

            @Override
            public Unit get(Object item) {
                return null;
            }
        };
    }

    @Override
    public DataComponent<Boolean> enchantmentGlintOverride() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Boolean glint) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (glint == null) {
                    return;
                }

                enchantments().apply(item, new ItemEnchantments(new HashMap<>(Map.of(Enchantment.LOYALTY, 1)), true));
                addItemFlags(tag, ItemFlag.HIDE_ENCHANTS);
            }

            @Override
            public Boolean get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return true;
            }
        };
    }

    @Override
    public DataComponent<Unit> intangibleProjectile() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {

            }

            @Override
            public Unit get(Object item) {
                return null;
            }
        };
    }

    @Override
    public DataComponent<ItemEnchantments> storedEnchantments() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ItemEnchantments itemEnchantments) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (itemEnchantments == null) {
                    tag.remove("StoredEnchantments");
                    return;
                }

                for (Map.Entry<Enchantment, Integer> entry : itemEnchantments.entrySet()) {
                    EnchantedBookItem.addEnchantment(itemStack, new EnchantmentInstance(CraftEnchantment.getRaw(entry.getKey()), entry.getValue()));
                }
            }

            @Override
            public ItemEnchantments get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();

                var enchants = EnchantmentHelper.getEnchantments(itemStack);
                HashMap<Enchantment, Integer> enchantments = new HashMap<>();
                for (Map.Entry<net.minecraft.world.item.enchantment.Enchantment, Integer> entry : enchants.entrySet()) {
                    enchantments.put(minecraftToBukkit(entry.getKey()), entry.getValue());
                }

                return new ItemEnchantments(enchantments, tag == null ? true : !hasItemFlag(tag, ItemFlag.HIDE_ENCHANTS));
            }
        };
    }

    @Override
    public DataComponent<ProfileProperties> profile() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ProfileProperties profileProperties) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (profileProperties == null) {
                    tag.remove("SkullOwner");
                    return;
                }

                net.minecraft.nbt.CompoundTag skullOwner = new net.minecraft.nbt.CompoundTag();
                skullOwner.putString("Name", "skull");
                net.minecraft.nbt.CompoundTag properties = new net.minecraft.nbt.CompoundTag();

                ListTag textures = new ListTag();
                net.minecraft.nbt.CompoundTag val = new net.minecraft.nbt.CompoundTag();
                val.putString("Value", profileProperties.properties().get("textures").stream().findFirst().get().value());
                textures.add(val);
                properties.put("textures", textures);

                skullOwner.put("Properties", properties);
                tag.put("SkullOwner", skullOwner);
            }

            @Override
            public ProfileProperties get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();

                if (tag == null || tag.isEmpty()) {
                    return null;
                }

                ProfileProperties profileProperties = new ProfileProperties(UUID.randomUUID(), "skull");
                net.minecraft.nbt.CompoundTag skullOwner = tag.getCompound("SkullOwner");


                net.minecraft.nbt.CompoundTag propertiesTag = skullOwner.getCompound("Properties");
                ListTag listTag = propertiesTag.getList("textures", 10);
                String textures = "";
                for (Tag tag1 : listTag) {
                    net.minecraft.nbt.CompoundTag compoundTag = (net.minecraft.nbt.CompoundTag) tag1;
                    textures = compoundTag.getString("Value");
                    break;
                }

                profileProperties.put("textures", new ProfileProperties.Property("textures", textures, null));
                return profileProperties;
            }
        };
    }

    @Override
    public DataComponent<Material> material() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Material material) {
                ItemStack itemStack = (ItemStack) item;
                if (material == Material.AIR) {
                    itemStack.setTag(null);
                }

                itemStack.setItem(CraftMagicNumbers.getItem(material));
            }

            @Override
            public Material get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return CraftMagicNumbers.getMaterial(itemStack.getItem());
            }
        };
    }

    @Override
    public DataComponent<DyedColor> dyedColor() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, DyedColor dyedColor) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (dyedColor == null) {
                    tag.remove("Color");
                    return;
                }

                setDisplayTag(tag, "Color", IntTag.valueOf(dyedColor.rgb()));
            }

            @Override
            public DyedColor get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                if (tag == null) {
                    return new DyedColor(Color.fromRGB(0), true);
                }

                return new DyedColor(Color.fromRGB(tag.getInt("Color")), hasItemFlag(tag, ItemFlag.HIDE_DYE));
            }
        };
    }

    @Override
    public DataComponent<PotionType> potionType() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, PotionType potionType) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getOrCreateTag();
                if (potionType == null) {
                    tag.remove("Potion");
                    return;
                }

                tag.putString("Potion", CraftPotionUtil.fromBukkit(new PotionData(potionType)));
            }

            @Override
            public PotionType get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.nbt.CompoundTag tag = itemStack.getTag();
                if (tag == null) {
                    return PotionType.AWKWARD;
                }

                return CraftPotionUtil.toBukkit(tag.getString("Potion")).getType();
            }
        };
    }
}
