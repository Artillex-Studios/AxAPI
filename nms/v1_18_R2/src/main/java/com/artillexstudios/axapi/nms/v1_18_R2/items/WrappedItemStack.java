package com.artillexstudios.axapi.nms.v1_18_R2.items;

import com.artillexstudios.axapi.utils.FastFieldAccessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WrappedItemStack implements com.artillexstudios.axapi.items.WrappedItemStack {
    private static final FastFieldAccessor HANDLE_ACCESSOR = FastFieldAccessor.forClassField(CraftItemStack.class, "handle");    private static final String DISPLAY_TAG = "display";
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

    @Override
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
    public void setName(Component name) {
        if (tag == null) {
            tag = new CompoundTag();
        }

        setDisplayTag(this.tag, "Name", StringTag.valueOf(GsonComponentSerializer.gson().serialize(name)));
    }

    @Override
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

    @Override
    public void setLore(List<Component> lore) {
        if (tag == null) {
            tag = new CompoundTag();
        }

        ListTag tag = new ListTag();
        List<String> jsonLore = asJson(lore);

        for (int i = 0; i < jsonLore.size(); i++) {
            tag.add(StringTag.valueOf(jsonLore.get(i)));
        }

        setDisplayTag(this.tag, "Lore", tag);
    }

    @Override
    public int getAmount() {
        return parent.getCount();
    }

    @Override
    public void setAmount(int amount) {
        parent.setCount(amount);
    }

    @Override
    public int getCustomModelData() {
        return tag != null ? tag.getInt("CustomModelData") : 0;
    }

    @Override
    public void setCustomModelData(int customModelData) {
        if (tag == null) {
            tag = new CompoundTag();
        }

        if (customModelData == -1) {
            tag.remove("CustomModelData");
        } else {
            tag.putInt("CustomModelData", customModelData);
        }
    }

    @Override
    public void setMaterial(Material material) {
        if (material == Material.AIR) {
            this.parent.setTag(null);
        }

        this.parent.setItem(Registry.ITEM.getOptional(CraftNamespacedKey.toMinecraft(material.getKey())).orElseThrow());
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        ListTag tags = this.parent.getEnchantmentTags();
        HashMap<Enchantment, Integer> foundEnchants = new HashMap<>(tags.size());

        for (int i = 0; i < tags.size(); i++) {
            CompoundTag compound = (CompoundTag) tags.get(i);
            String key = compound.getString("id");
            int level = 0xffff & compound.getShort("lvl");
            Enchantment found = Enchantment.getByKey(NamespacedKey.minecraft(key));
            if (found != null) {
                foundEnchants.put(found, level);
            }
        }

        return foundEnchants;
    }

    @Override
    public int getEnchantmentLevel(Enchantment enchantment) {
        ListTag tags = this.parent.getEnchantmentTags();
        String enchantKey = enchantment.getKey().toString();

        for (int i = 0; i < tags.size(); i++) {
            CompoundTag compound = (CompoundTag) tags.get(i);
            String key = compound.getString("id");
            if (!key.equals(enchantKey)) {
                continue;
            }

            return 0xffff & compound.getShort("lvl");
        }
        return 0;
    }

    @Override
    public void addItemFlags(ItemFlag... itemFlags) {
        if (tag == null) {
            tag = new CompoundTag();
        }

        byte flag = parent.getTag() != null ? parent.getTag().contains("HideFlags", 99) ? (byte) this.parent.getTag().getInt("HideFlags") : 0 : 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag |= (byte) (itemFlag.ordinal() << 1);
        }

        tag.putInt("HideFlags", flag);
    }

    @Override
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

    @Override
    public Set<ItemFlag> getItemFlags() {
        Set<ItemFlag> flags = new HashSet<>(ItemFlag.values().length);

        for (ItemFlag flag : ItemFlag.values()) {
            if (!hasItemFlag(flag)) continue;
            flags.add(flag);
        }

        return flags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag itemFlag) {
        byte flag = parent.getTag() != null ? tag.contains("HideFlags", 99) ? (byte) tag.getInt("HideFlags") : 0 : 0;
        int bitModifier = (byte) (1 << itemFlag.ordinal());
        return (flag & bitModifier) == bitModifier;
    }

    @Override
    public com.artillexstudios.axapi.items.nbt.CompoundTag getCompoundTag() {
        if (tag == null) {
            tag = new CompoundTag();
        }

        return new com.artillexstudios.axapi.nms.v1_18_R2.items.nbt.CompoundTag(tag);
    }

    @Override
    public org.bukkit.inventory.ItemStack toBukkit() {
        return CraftItemStack.asCraftMirror(parent);
    }

    @Override
    public void setTexture(String texture) {
        if (tag == null) {
            tag = new CompoundTag();
        }

        CompoundTag skullOwner = new CompoundTag();
        skullOwner.putString("Name", "skull");
        CompoundTag properties = new CompoundTag();

        ListTag textures = new ListTag();
        CompoundTag value = new CompoundTag();
        value.putString("Value", texture);
        textures.add(value);
        properties.put("textures", textures);

        skullOwner.put("Properties", properties);
        tag.put("SkullOwner", skullOwner);
    }

    @Override
    public boolean hasTag() {
        return tag != null;
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

    @Override
    public ItemStack getParent() {
        return parent;
    }
}
