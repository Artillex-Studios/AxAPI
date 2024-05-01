package com.artillexstudios.axapi.nms.v1_20_R1.items;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.items.component.DyedColor;
import com.artillexstudios.axapi.items.component.ItemEnchantments;
import com.artillexstudios.axapi.items.component.ItemLore;
import com.artillexstudios.axapi.items.component.ProfileProperties;
import com.artillexstudios.axapi.items.component.Unbreakable;
import com.artillexstudios.axapi.items.component.Unit;
import com.artillexstudios.axapi.nms.v1_20_R1.ItemStackSerializer;
import com.artillexstudios.axapi.utils.FastFieldAccessor;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WrappedItemStack implements com.artillexstudios.axapi.items.WrappedItemStack {
    private static final FastFieldAccessor HANDLE_ACCESSOR = FastFieldAccessor.forClassField(CraftItemStack.class, "handle");
    private static final String DISPLAY_TAG = "display";
    private final ItemStack parent;
    private final org.bukkit.inventory.ItemStack bukkitStack;
    private CompoundTag tag;

    public WrappedItemStack(org.bukkit.inventory.ItemStack itemStack) {
        this.parent = CraftItemStack.asNMSCopy(itemStack);
        bukkitStack = itemStack;
        tag = parent.getTag();
    }

    public WrappedItemStack(ItemStack itemStack) {
        this.parent = itemStack;
        this.bukkitStack = parent.getBukkitStack();
        tag = parent.getTag();
    }

    private static void setDisplayTag(CompoundTag compoundTag, String key, @Nullable Tag value) {
        final CompoundTag display = compoundTag.getCompound(DISPLAY_TAG);

        if (!compoundTag.contains(DISPLAY_TAG)) {
            compoundTag.put(DISPLAY_TAG, display);
        }

        display.remove(key);

        if (value != null) {
            display.put(key, value);
        }
    }

    public static List<String> asJson(List<? extends Component> adventures) {
        List<String> jsons = new ArrayList<>(adventures.size());
        Iterator<? extends Component> var2 = adventures.iterator();

        while (var2.hasNext()) {
            Component component = var2.next();
            jsons.add(GsonComponentSerializer.gson().serialize(component));
        }

        return jsons;
    }

    public static ArrayList<Component> asAdventureFromJson(List<String> jsonStrings) {
        ArrayList<Component> adventures = new ArrayList<>(jsonStrings.size());
        Iterator<String> var2 = jsonStrings.iterator();

        while (var2.hasNext()) {
            String json = var2.next();
            adventures.add(GsonComponentSerializer.gson().deserialize(json));
        }

        return adventures;
    }

    public static Enchantment minecraftToBukkit(net.minecraft.world.item.enchantment.Enchantment minecraft) {
        Preconditions.checkArgument(minecraft != null);
        Registry<net.minecraft.world.item.enchantment.Enchantment> registry = BuiltInRegistries.ENCHANTMENT;
        Enchantment bukkit = org.bukkit.Registry.ENCHANTMENT.get(CraftNamespacedKey.fromMinecraft((registry.getResourceKey(minecraft).orElseThrow()).location()));
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }

    public List<Component> getLore() {
        if (tag == null) {
            tag = new CompoundTag();
        }

        CompoundTag parentTag = this.tag;

        if (parentTag.contains(DISPLAY_TAG)) {
            CompoundTag display = parentTag.getCompound(DISPLAY_TAG);
            if (!display.contains("Lore")) return Collections.emptyList();

            ListTag list = display.getList("Lore", CraftMagicNumbers.NBT.TAG_STRING);
            ArrayList<String> lore = new ArrayList<>(list.size());
            for (int index = 0; index < list.size(); index++) {
                String line = list.getString(index);
                lore.add(line);
            }

            return asAdventureFromJson(lore);
        }

        return Collections.emptyList();
    }

    public void addItemFlags(ItemFlag... itemFlags) {
        if (tag == null) {
            tag = new CompoundTag();
        }

        byte flag = tag.contains("HideFlags", 99) ? (byte) tag.getInt("HideFlags") : 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag |= (byte) (itemFlag.ordinal() << 1);
        }

        tag.putInt("HideFlags", flag);
    }

    public void removeItemFlags(ItemFlag... itemFlags) {
        if (tag == null) {
            tag = new CompoundTag();
        }

        byte flag = parent.getTag() != null ? parent.getTag().contains("HideFlags", 99) ? (byte) this.parent.getTag().getInt("HideFlags") : 0 : 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag &= (byte) ~(byte) (1 << itemFlag.ordinal());
        }

        tag.putInt("HideFlags", flag);
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        byte flag = parent.getTag() != null ? tag.contains("HideFlags", 99) ? (byte) tag.getInt("HideFlags") : 0 : 0;
        int bitModifier = (byte) (1 << itemFlag.ordinal());
        return (flag & bitModifier) == bitModifier;
    }

    public com.artillexstudios.axapi.items.nbt.CompoundTag getCompoundTag() {
        if (tag == null) {
            tag = new CompoundTag();
        }

        return new com.artillexstudios.axapi.nms.v1_20_R1.items.nbt.CompoundTag(tag);
    }

    @Override
    public <T> void set(DataComponent<T> component, T value) {
        if (component == DataComponent.CUSTOM_DATA) {
            if (value == null) {
                return;
            }

            this.tag = ((com.artillexstudios.axapi.nms.v1_20_R1.items.nbt.CompoundTag) value).getParent();
        } else if (component == DataComponent.MAX_STACK_SIZE) {
            return;
        } else if (component == DataComponent.MAX_DAMAGE) {
        } else if (component == DataComponent.DAMAGE) {
            if (value == null) {
                parent.setDamageValue(1);
                return;
            }

            parent.setDamageValue((Integer) value);
        } else if (component == DataComponent.UNBREAKABLE) {
            if (value == null) {
                getCompoundTag().remove("Unbreakable");
                return;
            }
            boolean showInTooltip = ((com.artillexstudios.axapi.items.component.Unbreakable) value).showInTooltip();
            if (!showInTooltip) {
                addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }

            getCompoundTag().putBoolean("Unbreakable", true);
        } else if (component == DataComponent.CUSTOM_NAME) {
            if (this.tag == null) {
                this.tag = new CompoundTag();
            }

            if (value == null) {
                setDisplayTag(this.tag, "Name", null);
                return;
            }

            setDisplayTag(this.tag, "Name", StringTag.valueOf(GsonComponentSerializer.gson().serialize((Component) value)));
        } else if (component == DataComponent.ITEM_NAME) {
            if (this.tag == null) {
                this.tag = new CompoundTag();
            }

            if (value == null) {
                setDisplayTag(this.tag, "Name", null);
                return;
            }

            setDisplayTag(this.tag, "Name", StringTag.valueOf(GsonComponentSerializer.gson().serialize((Component) value)));
        } else if (component == DataComponent.LORE) {
            if (this.tag == null) {
                this.tag = new CompoundTag();
            }

            if (value == null) {
                setDisplayTag(this.tag, "Lore", null);
                return;
            }

            ListTag tag = new ListTag();
            List<String> jsonLore = asJson(((ItemLore) value).lines());

            for (int i = 0; i < jsonLore.size(); i++) {
                tag.add(StringTag.valueOf(jsonLore.get(i)));
            }

            setDisplayTag(this.tag, "Lore", tag);
        } else if (component == DataComponent.RARITY) {

        } else if (component == DataComponent.ENCHANTMENTS) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            if (value == null) {
                tag.remove("Enchantments");
                return;
            }

            var enchants = (ItemEnchantments) value;
            HashMap<net.minecraft.world.item.enchantment.Enchantment, Integer> enchantments = new HashMap<>();
            for (Object2IntMap.Entry<Enchantment> entry : enchants.entrySet()) {
                enchantments.put(CraftEnchantment.getRaw(entry.getKey()), entry.getIntValue());
            }

            EnchantmentHelper.setEnchantments(enchantments, parent);
        } else if (component == DataComponent.CUSTOM_MODEL_DATA) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            if (value == null) {
                tag.remove("CustomModelData");
            } else {
                tag.putInt("CustomModelData", (Integer) value);
            }
        } else if (component == DataComponent.HIDE_ADDITIONAL_TOOLTIP) {
            if (value == null) {
                removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
                return;
            }

            addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        } else if (component == DataComponent.HIDE_TOOLTIP) {
            if (value == null) {
                removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                return;
            }

            addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else if (component == DataComponent.REPAIR_COST) {
            parent.setRepairCost((Integer) value);
        } else if (component == DataComponent.CREATIVE_SLOT_LOCK) {

        } else if (component == DataComponent.ENCHANTMENT_GLINT_OVERRIDE) {
            addItemFlags(ItemFlag.HIDE_ENCHANTS);
            set(DataComponent.ENCHANTMENTS, get(DataComponent.ENCHANTMENTS).add(Enchantment.LOYALTY, 1));
        } else if (component == DataComponent.INTANGIBLE_PROJECTILE) {

        } else if (component == DataComponent.STORED_ENCHANTMENTS) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            if (value == null) {
                tag.remove("StoredEnchantments");
                return;
            }

            var enchants = (ItemEnchantments) value;
            for (Object2IntMap.Entry<Enchantment> entry : enchants.entrySet()) {
                EnchantedBookItem.addEnchantment(parent, new EnchantmentInstance(CraftEnchantment.getRaw(entry.getKey()), entry.getIntValue()));
            }
        } else if (component == DataComponent.PROFILE) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            ProfileProperties profileProperties = (ProfileProperties) value;

            CompoundTag skullOwner = new CompoundTag();
            skullOwner.putString("Name", "skull");
            CompoundTag properties = new CompoundTag();

            ListTag textures = new ListTag();
            CompoundTag val = new CompoundTag();
            val.putString("Value", profileProperties.properties().get("textures").stream().findFirst().get().value());
            textures.add(val);
            properties.put("textures", textures);

            skullOwner.put("Properties", properties);
            tag.put("SkullOwner", skullOwner);
        } else if (component == DataComponent.MATERIAL) {
            var material = (Material) value;
            if (material == Material.AIR) {
                this.parent.setTag(null);
            }

            this.parent.setItem(BuiltInRegistries.ITEM.getOptional(CraftNamespacedKey.toMinecraft(material.getKey())).orElseThrow());
        } else if (component == DataComponent.DYED_COLOR) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            if (value == null) {
                tag.remove("Color");
                return;
            }
            var color = (DyedColor) value;

            setDisplayTag(this.tag, "Color", IntTag.valueOf(color.rgb()));
        } else if (component == DataComponent.POTION_CONTENTS) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            if (value == null) {
                tag.remove("Potion");
                return;
            }

            tag.putString("Potion", CraftPotionUtil.fromBukkit(new PotionData((PotionType) value)));
        }
    }

    @Override
    public <T> T get(DataComponent<T> component) {
        if (component == DataComponent.CUSTOM_DATA) {
            return (T) getCompoundTag();
        } else if (component == DataComponent.MAX_STACK_SIZE) {
            return (T) Integer.valueOf(0);
        } else if (component == DataComponent.MAX_DAMAGE) {
            return (T) Integer.valueOf(0);
        } else if (component == DataComponent.DAMAGE) {
            return (T) Integer.valueOf(parent.getDamageValue());
        } else if (component == DataComponent.UNBREAKABLE) {
            boolean contains = getCompoundTag().contains("Unbreakable");
            return contains ? (T) new Unbreakable(!hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)) : null;
        } else if (component == DataComponent.CUSTOM_NAME) {
            return (T) getName();
        } else if (component == DataComponent.ITEM_NAME) {
            return (T) getName();
        } else if (component == DataComponent.LORE) {
            return (T) new ItemLore(getLore());
        } else if (component == DataComponent.RARITY) {
            return null;
        } else if (component == DataComponent.ENCHANTMENTS) {
            var enchants = EnchantmentHelper.getEnchantments(parent);
            Object2IntAVLTreeMap<Enchantment> enchantments = new Object2IntAVLTreeMap<>();
            for (Map.Entry<net.minecraft.world.item.enchantment.Enchantment, Integer> entry : enchants.entrySet()) {
                enchantments.put(minecraftToBukkit(entry.getKey()), entry.getValue().intValue());
            }

            return (T) new ItemEnchantments(enchantments, !hasItemFlag(ItemFlag.HIDE_ENCHANTS));
        } else if (component == DataComponent.CUSTOM_MODEL_DATA) {
            return (T) Integer.valueOf(getCompoundTag().getInt("CustomModelData"));
        } else if (component == DataComponent.HIDE_ADDITIONAL_TOOLTIP) {
            return hasItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS) ? (T) Unit.INSTANCE : null;
        } else if (component == DataComponent.HIDE_TOOLTIP) {
            return hasItemFlag(ItemFlag.HIDE_ENCHANTS) ? (T) Unit.INSTANCE : null;
        } else if (component == DataComponent.REPAIR_COST) {
            return (T) Integer.valueOf(parent.getBaseRepairCost());
        } else if (component == DataComponent.CREATIVE_SLOT_LOCK) {
            return null;
        } else if (component == DataComponent.ENCHANTMENT_GLINT_OVERRIDE) {
            set(DataComponent.ENCHANTMENTS, get(DataComponent.ENCHANTMENTS).add(Enchantment.LOYALTY, 1));
            addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else if (component == DataComponent.INTANGIBLE_PROJECTILE) {
            return null;
        } else if (component == DataComponent.STORED_ENCHANTMENTS) {
            var enchants = EnchantmentHelper.getEnchantments(parent);
            Object2IntAVLTreeMap<Enchantment> enchantments = new Object2IntAVLTreeMap<>();
            for (Map.Entry<net.minecraft.world.item.enchantment.Enchantment, Integer> entry : enchants.entrySet()) {
                enchantments.put(minecraftToBukkit(entry.getKey()), entry.getValue().intValue());
            }

            return (T) new ItemEnchantments(enchantments, !hasItemFlag(ItemFlag.HIDE_ENCHANTS));
        } else if (component == DataComponent.PROFILE) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            ProfileProperties profileProperties = new ProfileProperties(UUID.randomUUID(), "skull");
            CompoundTag skullOwner = tag.getCompound("SkullOwner");
            CompoundTag propertiesTag = skullOwner.getCompound("Properties");
            ListTag tag = propertiesTag.getList("textures", 10);
            String textures = "";
            for (Tag tag1 : tag) {
                CompoundTag compoundTag = (CompoundTag) tag1;
                textures = compoundTag.getString("Value");
                break;
            }

            profileProperties.put("textures", new ProfileProperties.Property("textures", textures, null));
            return (T) profileProperties;
        } else if (component == DataComponent.MATERIAL) {
            return (T) CraftMagicNumbers.getMaterial(this.parent.getItem());
        } else if (component == DataComponent.DYED_COLOR) {
            if (tag == null) {
                tag = new CompoundTag();
            }


            return (T) new DyedColor(Color.fromRGB(tag.getInt("Color")), hasItemFlag(ItemFlag.HIDE_DYE));
        } else if (component == DataComponent.POTION_CONTENTS) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            return (T) CraftPotionUtil.toBukkit(tag.getString("Potion")).getType();
        }

        return null;
    }

    public Component getName() {
        if (tag == null) {
            return Component.empty();
        }

        if (!tag.contains(DISPLAY_TAG)) {
            return Component.empty();
        }

        CompoundTag display = tag.getCompound(DISPLAY_TAG);
        if (display.contains("Name")) {
            return net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(display.getString("Name"));
        }

        return Component.empty();
    }

    @Override
    public int getAmount() {
        return parent.getCount();
    }

    @Override
    public void setAmount(int amount) {
        this.parent.setCount(amount);
    }

    @Override
    public org.bukkit.inventory.ItemStack toBukkit() {
        return CraftItemStack.asCraftMirror(parent);
    }

    @Override
    public String toSNBT() {
        var compoundTag = (net.minecraft.nbt.CompoundTag) parent.save(new CompoundTag());
        compoundTag.putInt("DataVersion", CraftMagicNumbers.INSTANCE.getDataVersion());
        return new SnbtPrinterTagVisitor().visit(compoundTag);
    }

    @Override
    public byte[] serialize() {
        return ItemStackSerializer.INSTANCE.serializeAsBytes(CraftItemStack.asBukkitCopy(parent));
    }

    @Override
    public void finishEdit() {
        if (tag == null || tag.isEmpty()) {
            if (CraftItemStack.class.isAssignableFrom(bukkitStack.getClass())) {
                CraftItemStack craftItemStack = (CraftItemStack) bukkitStack;
                ItemStack handle = HANDLE_ACCESSOR.get(craftItemStack);
                handle.setTag(null);
            } else {
                parent.setTag(null);
                org.bukkit.inventory.ItemStack bukkitItem = CraftItemStack.asCraftMirror(parent);
                bukkitStack.setItemMeta(bukkitItem.getItemMeta());
            }
            return;
        }

        if (CraftItemStack.class.isAssignableFrom(bukkitStack.getClass())) {
            CraftItemStack craftItemStack = (CraftItemStack) bukkitStack;
            ItemStack handle = HANDLE_ACCESSOR.get(craftItemStack);
            handle.setTag(tag);
        } else {
            parent.setTag(tag);
            org.bukkit.inventory.ItemStack bukkitItem = CraftItemStack.asCraftMirror(parent);
            bukkitStack.setItemMeta(bukkitItem.getItemMeta());
        }
    }
}
