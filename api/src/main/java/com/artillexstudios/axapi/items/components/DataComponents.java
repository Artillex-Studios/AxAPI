package com.artillexstudios.axapi.items.components;

import com.artillexstudios.axapi.items.component.type.Unit;
import com.artillexstudios.axapi.items.components.data.CustomModelData;
import com.artillexstudios.axapi.items.components.data.DyedItemColor;
import com.artillexstudios.axapi.items.components.data.ItemEnchantments;
import com.artillexstudios.axapi.items.components.data.ItemLore;
import com.artillexstudios.axapi.items.components.data.PotionContents;
import com.artillexstudios.axapi.items.components.data.Rarity;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.utils.ResolvableProfile;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public final class DataComponents {
    public static final DataComponent<CompoundTag> CUSTOM_DATA = fetch("custom_data");
    public static final DataComponent<Integer> MAX_STACK_SIZE = fetch("max_stack_size");
    public static final DataComponent<Integer> MAX_DAMAGE = fetch("max_damage");
    public static final DataComponent<Integer> DAMAGE = fetch("damage");
    public static final DataComponent<Component> CUSTOM_NAME = fetch("custom_name");
    public static final DataComponent<Component> ITEM_NAME = fetch("item_name");
    public static final DataComponent<Key> ITEM_MODEL = fetch("item_model");
    public static final DataComponent<Key> TOOLTIP_STYLE = fetch("tooltip_style");
    public static final DataComponent<ItemLore> LORE = fetch("lore");
    public static final DataComponent<Integer> REPAIR_COST = fetch("repair_cost");
    public static final DataComponent<Rarity> RARITY = fetch("rarity");
    public static final DataComponent<ItemEnchantments> ENCHANTMENTS = fetch("enchantments");
    public static final DataComponent<ItemEnchantments> STORED_ENCHANTMENTS = fetch("stored_enchantments");
    public static final DataComponent<CustomModelData> CUSTOM_MODEL_DATA = fetch("custom_model_data");
    public static final DataComponent<Boolean> ENCHANTMENT_GLINT_OVERRIDE = fetch("enchantment_glint_override");
    public static final DataComponent<ResolvableProfile> PROFILE = fetch("profile");
    public static final DataComponent<Material> MATERIAL = fetch("material");
    public static final DataComponent<DyedItemColor> DYED_COLOR = fetch("dyed_color");
    public static final DataComponent<PotionContents> POTION_CONTENTS = fetch("potion_contents");
    public static final DataComponent<Unit> UNBREAKABLE = fetch("unbreakable");
    public static final DataComponent<Float> MINIMUM_ATTACK_CHARGE = fetch("minimum_attack_charge");

    private static <T extends DataComponent<?>> T fetch(String id) {
        return NMSHandlers.getNmsHandler().getDataComponent(id);
    }
}
