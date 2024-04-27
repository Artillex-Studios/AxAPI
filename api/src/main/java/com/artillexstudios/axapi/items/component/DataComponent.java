package com.artillexstudios.axapi.items.component;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

@SuppressWarnings("InstantiationOfUtilityClass")
public class DataComponent<T> {
    public static final DataComponent<CompoundTag> CUSTOM_DATA = new DataComponent<>();
    public static final DataComponent<Integer> MAX_STACK_SIZE = new DataComponent<>();
    public static final DataComponent<Integer> MAX_DAMAGE = new DataComponent<>();
    public static final DataComponent<Integer> DAMAGE = new DataComponent<>();
    public static final DataComponent<Unbreakable> UNBREAKABLE = new DataComponent<>();
    public static final DataComponent<Component> CUSTOM_NAME = new DataComponent<>();
    public static final DataComponent<Component> ITEM_NAME = new DataComponent<>();
    public static final DataComponent<ItemLore> LORE = new DataComponent<>();
    public static final DataComponent<Rarity> RARITY = new DataComponent<>();
    public static final DataComponent<ItemEnchantments> ENCHANTMENTS = new DataComponent<>();
    public static final DataComponent<Integer> CUSTOM_MODEL_DATA = new DataComponent<>();
    public static final DataComponent<Unit> HIDE_ADDITIONAL_TOOLTIP = new DataComponent<>();
    public static final DataComponent<Unit> HIDE_TOOLTIP = new DataComponent<>();
    public static final DataComponent<Integer> REPAIR_COST = new DataComponent<>();
    public static final DataComponent<Unit> CREATIVE_SLOT_LOCK = new DataComponent<>();
    public static final DataComponent<Boolean> ENCHANTMENT_GLINT_OVERRIDE = new DataComponent<>();
    public static final DataComponent<Unit> INTANGIBLE_PROJECTILE = new DataComponent<>();
    public static final DataComponent<ItemEnchantments> STORED_ENCHANTMENTS = new DataComponent<>();
    public static final DataComponent<ProfileProperties> PROFILE = new DataComponent<>();
    public static final DataComponent<Material> MATERIAL = new DataComponent<>();
    public static final DataComponent<DyedColor> DYED_COLOR = new DataComponent<>();
    public static final DataComponent<PotionType> POTION_CONTENTS = new DataComponent<>();
}
