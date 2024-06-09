package com.artillexstudios.axapi.items.component;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

public class DataComponents {
    private static DataComponentImpl dataComponentImpl;

    private static DataComponent<CompoundTag> customData() {
        return dataComponentImpl.customData();
    }

    private static DataComponent<Integer> maxStackSize() {
        return dataComponentImpl.maxStackSize();
    }

    private static DataComponent<Integer> maxDamage() {
        return dataComponentImpl.maxDamage();
    }

    private static DataComponent<Integer> damage() {
        return dataComponentImpl.damage();
    }

    private static DataComponent<Unbreakable> unbreakable() {
        return dataComponentImpl.unbreakable();
    }

    private static DataComponent<Component> customName() {
        return dataComponentImpl.customName();
    }

    private static DataComponent<Component> itemName() {
        return dataComponentImpl.itemName();
    }
    private static DataComponent<ItemLore> lore() {
        return dataComponentImpl.lore();
    }

    private static DataComponent<Rarity> rarity() {
        return dataComponentImpl.rarity();
    }
    private static DataComponent<ItemEnchantments> enchantments() {
        return dataComponentImpl.enchantments();
    }

    private static DataComponent<Integer> customModelData() {
        return dataComponentImpl.customModelData();
    }

    private static DataComponent<Unit> hideAdditionalTooltip() {
        return dataComponentImpl.hideAdditionalTooltip();
    }

    private static DataComponent<Unit> hideTooltip() {
        return dataComponentImpl.hideTooltip();
    }

    private static DataComponent<Integer> repairCost() {
        return dataComponentImpl.repairCost();
    }

    private static DataComponent<Unit> creativeSlotLock() {
        return dataComponentImpl.creativeSlotLock();
    }

    private static DataComponent<Boolean> enchantmentGlintOverride() {
        return dataComponentImpl.enchantmentGlintOverride();
    }

    private static DataComponent<Unit> intangibleProjectile() {
        return dataComponentImpl.intangibleProjectile();
    }

    private static DataComponent<ItemEnchantments> storedEnchantments() {
        return dataComponentImpl.storedEnchantments();
    }

    private static DataComponent<ProfileProperties> profile() {
        return dataComponentImpl.profile();
    }

    private static DataComponent<Material> material() {
        return dataComponentImpl.material();
    }

    private static DataComponent<DyedColor> dyedColor() {
        return dataComponentImpl.dyedColor();
    }

    private static DataComponent<PotionType> potionType() {
        return dataComponentImpl.potionType();
    }


    public static void setDataComponentImpl(DataComponentImpl dataComponentImpl) {
        if (DataComponents.dataComponentImpl != null) {
            return;
        }

        DataComponents.dataComponentImpl = dataComponentImpl;
    }
}
