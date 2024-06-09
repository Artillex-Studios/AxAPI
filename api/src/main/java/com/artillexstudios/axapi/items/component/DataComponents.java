package com.artillexstudios.axapi.items.component;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

public class DataComponents {
    private static DataComponentImpl dataComponentImpl;

    public static DataComponent<CompoundTag> customData() {
        return dataComponentImpl.customData();
    }

    public static DataComponent<Integer> maxStackSize() {
        return dataComponentImpl.maxStackSize();
    }

    public static DataComponent<Integer> maxDamage() {
        return dataComponentImpl.maxDamage();
    }

    public static DataComponent<Integer> damage() {
        return dataComponentImpl.damage();
    }

    public static DataComponent<Unbreakable> unbreakable() {
        return dataComponentImpl.unbreakable();
    }

    public static DataComponent<Component> customName() {
        return dataComponentImpl.customName();
    }

    public static DataComponent<Component> itemName() {
        return dataComponentImpl.itemName();
    }

    public static DataComponent<ItemLore> lore() {
        return dataComponentImpl.lore();
    }

    public static DataComponent<Rarity> rarity() {
        return dataComponentImpl.rarity();
    }

    public static DataComponent<ItemEnchantments> enchantments() {
        return dataComponentImpl.enchantments();
    }

    public static DataComponent<Integer> customModelData() {
        return dataComponentImpl.customModelData();
    }

    public static DataComponent<Unit> hideAdditionalTooltip() {
        return dataComponentImpl.hideAdditionalTooltip();
    }

    public static DataComponent<Unit> hideTooltip() {
        return dataComponentImpl.hideTooltip();
    }

    public static DataComponent<Integer> repairCost() {
        return dataComponentImpl.repairCost();
    }

    public static DataComponent<Unit> creativeSlotLock() {
        return dataComponentImpl.creativeSlotLock();
    }

    public static DataComponent<Boolean> enchantmentGlintOverride() {
        return dataComponentImpl.enchantmentGlintOverride();
    }

    public static DataComponent<Unit> intangibleProjectile() {
        return dataComponentImpl.intangibleProjectile();
    }

    public static DataComponent<ItemEnchantments> storedEnchantments() {
        return dataComponentImpl.storedEnchantments();
    }

    public static DataComponent<ProfileProperties> profile() {
        return dataComponentImpl.profile();
    }

    public static DataComponent<Material> material() {
        return dataComponentImpl.material();
    }

    public static DataComponent<DyedColor> dyedColor() {
        return dataComponentImpl.dyedColor();
    }

    public static DataComponent<PotionType> potionType() {
        return dataComponentImpl.potionType();
    }


    public static void setDataComponentImpl(DataComponentImpl dataComponentImpl) {
        if (DataComponents.dataComponentImpl != null) {
            return;
        }

        DataComponents.dataComponentImpl = dataComponentImpl;
    }
}
