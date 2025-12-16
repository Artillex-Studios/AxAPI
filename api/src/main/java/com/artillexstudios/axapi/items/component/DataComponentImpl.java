package com.artillexstudios.axapi.items.component;

import com.artillexstudios.axapi.items.component.type.CustomModelData;
import com.artillexstudios.axapi.items.component.type.DyedColor;
import com.artillexstudios.axapi.items.component.type.ItemEnchantments;
import com.artillexstudios.axapi.items.component.type.ItemLore;
import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.items.component.type.Rarity;
import com.artillexstudios.axapi.items.component.type.Unbreakable;
import com.artillexstudios.axapi.items.component.type.Unit;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

public interface DataComponentImpl {
    String DISPLAY_TAG = "display";

    DataComponent<CompoundTag> customData();

    DataComponent<Integer> maxStackSize();

    DataComponent<Integer> maxDamage();

    DataComponent<Integer> damage();

    DataComponent<Unbreakable> unbreakable();

    DataComponent<Component> customName();

    DataComponent<Component> itemName();

    DataComponent<Key> itemModel();

    DataComponent<ItemLore> lore();

    DataComponent<Rarity> rarity();

    DataComponent<ItemEnchantments> enchantments();

    DataComponent<CustomModelData> customModelData();

    DataComponent<Unit> hideAdditionalTooltip();

    DataComponent<Unit> hideTooltip();

    DataComponent<Integer> repairCost();

    DataComponent<Unit> creativeSlotLock();

    DataComponent<Boolean> enchantmentGlintOverride();

    DataComponent<Unit> intangibleProjectile();

    DataComponent<ItemEnchantments> storedEnchantments();

    DataComponent<ProfileProperties> profile();

    DataComponent<Material> material();

    DataComponent<DyedColor> dyedColor();

    DataComponent<PotionType> potionType();

    DataComponent<Key> tooltipStyle();
}
