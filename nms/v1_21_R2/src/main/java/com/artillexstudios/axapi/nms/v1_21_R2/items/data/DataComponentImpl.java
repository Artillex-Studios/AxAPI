package com.artillexstudios.axapi.nms.v1_21_R2.items.data;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.items.component.type.DyedColor;
import com.artillexstudios.axapi.items.component.type.ItemEnchantments;
import com.artillexstudios.axapi.items.component.type.ItemLore;
import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.items.component.type.Rarity;
import com.artillexstudios.axapi.items.component.type.Unbreakable;
import com.artillexstudios.axapi.items.component.type.Unit;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ResolvableProfile;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DataComponentImpl implements com.artillexstudios.axapi.items.component.DataComponentImpl {

    @Override
    public DataComponent<CompoundTag> customData() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, CompoundTag compoundTag) {
                ItemStack itemStack = (ItemStack) item;

                if (compoundTag == null || compoundTag.isEmpty()) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, CustomData.of((net.minecraft.nbt.CompoundTag) compoundTag.getParent()));
            }

            @Override
            public CompoundTag get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return new com.artillexstudios.axapi.nms.v1_21_R2.items.nbt.CompoundTag(itemStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, CustomData.of(new net.minecraft.nbt.CompoundTag())).copyTag());
            }
        };
    }

    @Override
    public DataComponent<Integer> maxStackSize() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {
                ItemStack itemStack = (ItemStack) item;
                if (integer == null || integer == 0) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.MAX_STACK_SIZE);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.MAX_STACK_SIZE, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return itemStack.getOrDefault(net.minecraft.core.component.DataComponents.MAX_STACK_SIZE, 0);
            }
        };
    }

    @Override
    public DataComponent<Integer> maxDamage() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {
                ItemStack itemStack = (ItemStack) item;
                if (integer == null || integer == 0) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.MAX_DAMAGE);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.MAX_DAMAGE, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return itemStack.getOrDefault(net.minecraft.core.component.DataComponents.MAX_DAMAGE, 0);
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
                    itemStack.remove(net.minecraft.core.component.DataComponents.DAMAGE);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.DAMAGE, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return itemStack.getOrDefault(net.minecraft.core.component.DataComponents.DAMAGE, 0);
            }
        };
    }

    @Override
    public DataComponent<Unbreakable> unbreakable() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unbreakable unbreakable) {
                ItemStack itemStack = (ItemStack) item;
                if (unbreakable == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.UNBREAKABLE);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.UNBREAKABLE, new net.minecraft.world.item.component.Unbreakable(unbreakable.showInTooltip()));
            }

            @Override
            public Unbreakable get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.world.item.component.Unbreakable vanilla = itemStack.get(net.minecraft.core.component.DataComponents.UNBREAKABLE);
                return new Unbreakable(vanilla != null && vanilla.showInTooltip());
            }
        };
    }

    @Override
    public DataComponent<Component> customName() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Component component) {
                ItemStack itemStack = (ItemStack) item;
                if (component == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, ComponentSerializer.INSTANCE.toVanilla(component));
            }

            @Override
            public Component get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.network.chat.Component component = itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
                return component == null ? Component.empty() : ComponentSerializer.INSTANCE.fromVanilla(component);
            }
        };
    }

    @Override
    public DataComponent<Component> itemName() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Component component) {
                ItemStack itemStack = (ItemStack) item;
                if (component == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.ITEM_NAME);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.ITEM_NAME, ComponentSerializer.INSTANCE.toVanilla(component));
            }

            @Override
            public Component get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.network.chat.Component component = itemStack.get(net.minecraft.core.component.DataComponents.ITEM_NAME);
                return component == null ? Component.empty() : ComponentSerializer.INSTANCE.fromVanilla(component);
            }
        };
    }

    @Override
    public DataComponent<Key> itemModel() {
        throw new UnsupportedOperationException("Your server version does not support this feature!");
    }

    @Override
    public DataComponent<ItemLore> lore() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ItemLore itemLore) {
                ItemStack itemStack = (ItemStack) item;
                if (itemLore == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.LORE);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.LORE, new net.minecraft.world.item.component.ItemLore(ComponentSerializer.INSTANCE.toVanillaList(itemLore.lines()), ComponentSerializer.INSTANCE.toVanillaList(itemLore.styledLines())));
            }

            @Override
            public ItemLore get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.world.item.component.ItemLore lore = itemStack.get(net.minecraft.core.component.DataComponents.LORE);
                return new ItemLore(lore == null ? List.of() : ComponentSerializer.INSTANCE.fromVanillaList(new ArrayList<>(lore.lines())), lore == null ? List.of() : ComponentSerializer.INSTANCE.fromVanillaList(new ArrayList<>(lore.styledLines())));
            }
        };
    }

    @Override
    public DataComponent<Rarity> rarity() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Rarity rarity) {
                ItemStack itemStack = (ItemStack) item;
                if (rarity == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.RARITY);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.RARITY, net.minecraft.world.item.Rarity.valueOf(rarity.name()));
            }

            @Override
            public Rarity get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return Rarity.valueOf(itemStack.getOrDefault(net.minecraft.core.component.DataComponents.RARITY, net.minecraft.world.item.Rarity.COMMON).name());
            }
        };
    }

    @Override
    public DataComponent<ItemEnchantments> enchantments() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ItemEnchantments itemEnchantments) {
                ItemStack itemStack = (ItemStack) item;
                if (itemEnchantments == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.ENCHANTMENTS);
                    return;
                }

                net.minecraft.world.item.enchantment.ItemEnchantments.Mutable vanilla = new net.minecraft.world.item.enchantment.ItemEnchantments.Mutable(net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
                vanilla.showInTooltip = itemEnchantments.showInTooltip();

                for (Map.Entry<Enchantment, Integer> e : itemEnchantments.entrySet()) {
                    vanilla.set(CraftEnchantment.bukkitToMinecraftHolder(e.getKey()), e.getValue());
                }

                itemStack.set(net.minecraft.core.component.DataComponents.ENCHANTMENTS, vanilla.toImmutable());
            }

            @Override
            public ItemEnchantments get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                net.minecraft.world.item.enchantment.ItemEnchantments vanilla = itemStack.get(net.minecraft.core.component.DataComponents.ENCHANTMENTS);
                HashMap<Enchantment, Integer> enchants = new HashMap<>();
                if (vanilla == null) {
                    return new ItemEnchantments(enchants, true);
                }

                vanilla.entrySet().forEach(e -> enchants.put(CraftEnchantment.minecraftToBukkit(e.getKey().value()), e.getIntValue()));

                return new ItemEnchantments(enchants, vanilla.showInTooltip);
            }
        };
    }

    @Override
    public DataComponent<com.artillexstudios.axapi.items.component.type.CustomModelData> customModelData() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, com.artillexstudios.axapi.items.component.type.CustomModelData modelData) {
                ItemStack itemStack = (ItemStack) item;
                if (modelData == null || modelData.floats().isEmpty()) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(modelData.floats().getFirst().intValue()));
            }

            @Override
            public com.artillexstudios.axapi.items.component.type.CustomModelData get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return new com.artillexstudios.axapi.items.component.type.CustomModelData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(List.of(((Integer) itemStack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value()).floatValue())), new ArrayList<>());
            }
        };
    }

    @Override
    public DataComponent<Unit> hideAdditionalTooltip() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
                ItemStack itemStack = (ItemStack) item;
                if (unit == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.HIDE_ADDITIONAL_TOOLTIP);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.HIDE_ADDITIONAL_TOOLTIP, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.get(net.minecraft.core.component.DataComponents.HIDE_ADDITIONAL_TOOLTIP) == null ? null : Unit.INSTANCE;
            }
        };
    }

    @Override
    public DataComponent<Unit> hideTooltip() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
                ItemStack itemStack = (ItemStack) item;
                if (unit == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.HIDE_TOOLTIP);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.HIDE_TOOLTIP, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.get(net.minecraft.core.component.DataComponents.HIDE_TOOLTIP) == null ? null : Unit.INSTANCE;
            }
        };
    }

    @Override
    public DataComponent<Integer> repairCost() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Integer integer) {
                ItemStack itemStack = (ItemStack) item;
                if (integer == null || integer == 0) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.REPAIR_COST);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.REPAIR_COST, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.getOrDefault(net.minecraft.core.component.DataComponents.REPAIR_COST, 0);
            }
        };
    }

    @Override
    public DataComponent<Unit> creativeSlotLock() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
                ItemStack itemStack = (ItemStack) item;
                if (unit == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.CREATIVE_SLOT_LOCK);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.CREATIVE_SLOT_LOCK, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.get(net.minecraft.core.component.DataComponents.CREATIVE_SLOT_LOCK) == null ? null : Unit.INSTANCE;
            }
        };
    }

    @Override
    public DataComponent<Boolean> enchantmentGlintOverride() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Boolean glint) {
                ItemStack itemStack = (ItemStack) item;
                if (glint == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE, glint);
            }

            @Override
            public Boolean get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.getOrDefault(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
            }
        };
    }

    @Override
    public DataComponent<Unit> intangibleProjectile() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
                ItemStack itemStack = (ItemStack) item;
                if (unit == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.INTANGIBLE_PROJECTILE);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.INTANGIBLE_PROJECTILE, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.get(net.minecraft.core.component.DataComponents.INTANGIBLE_PROJECTILE) == null ? null : Unit.INSTANCE;
            }
        };
    }

    @Override
    public DataComponent<ItemEnchantments> storedEnchantments() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ItemEnchantments itemEnchantments) {
                ItemStack itemStack = (ItemStack) item;
                if (itemEnchantments == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS);
                    return;
                }

                net.minecraft.world.item.enchantment.ItemEnchantments.Mutable vanilla = new net.minecraft.world.item.enchantment.ItemEnchantments.Mutable(net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
                vanilla.showInTooltip = itemEnchantments.showInTooltip();

                for (Map.Entry<Enchantment, Integer> e : itemEnchantments.entrySet()) {
                    vanilla.set(CraftEnchantment.bukkitToMinecraftHolder(e.getKey()), e.getValue());
                }

                itemStack.set(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS, vanilla.toImmutable());
            }

            @Override
            public ItemEnchantments get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.world.item.enchantment.ItemEnchantments vanilla = itemStack.get(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS);

                HashMap<Enchantment, Integer> enchants = new HashMap<>();
                if (vanilla == null) {
                    return new ItemEnchantments(enchants, true);
                }

                vanilla.entrySet().forEach(e -> enchants.put(CraftEnchantment.minecraftToBukkit(e.getKey().value()), e.getIntValue()));

                return new ItemEnchantments(enchants, vanilla.showInTooltip);
            }
        };
    }

    @Override
    public DataComponent<ProfileProperties> profile() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ProfileProperties profileProperties) {
                ItemStack itemStack = (ItemStack) item;
                if (profileProperties == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.PROFILE);
                    return;
                }

                GameProfile gameProfile = new GameProfile(profileProperties.uuid(), profileProperties.name());

                for (Map.Entry<String, ProfileProperties.Property> entry : profileProperties.properties().entries()) {
                    var property = entry.getValue();
                    gameProfile.getProperties().put(entry.getKey(), new Property(property.name(), property.value(), property.signature()));
                }

                itemStack.set(net.minecraft.core.component.DataComponents.PROFILE, new ResolvableProfile(gameProfile));
            }

            @Override
            public ProfileProperties get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                ResolvableProfile profile = itemStack.get(net.minecraft.core.component.DataComponents.PROFILE);

                if (profile == null) {
                    return new ProfileProperties(new UUID(0, 0), "---");
                }

                var profileProperties = new ProfileProperties(profile.id().orElse(new UUID(0, 0)), profile.name().orElse(""));
                profile.properties().forEach((k, v) -> profileProperties.put(k, new ProfileProperties.Property(v.name(), v.value(), v.signature())));

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
                DataComponentPatch patch = itemStack.getComponentsPatch();
                itemStack.setItem(CraftMagicNumbers.getItem(material));
                itemStack.restorePatch(patch);
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
                if (dyedColor == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.DYED_COLOR);
                    return;
                }

                if (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.TIPPED_ARROW)) {
                    PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
                    if (contents == null || contents == PotionContents.EMPTY) {
                        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(dyedColor.rgb()), List.of(), Optional.empty()));
                    }
                }

                itemStack.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new DyedItemColor(dyedColor.rgb(), dyedColor.showInTooltip()));
            }

            @Override
            public DyedColor get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                var color = itemStack.get(net.minecraft.core.component.DataComponents.DYED_COLOR);
                if (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.TIPPED_ARROW)) {
                    PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
                    if (contents != null && contents != PotionContents.EMPTY) {
                        return new DyedColor(color == null ? Color.fromRGB(contents.customColor().orElse(0)) : Color.fromRGB(color.rgb()), true);
                    }
                }

                return new DyedColor(color == null ? Color.fromRGB(0) : Color.fromRGB(color.rgb()), color == null ? true : color.showInTooltip());
            }
        };
    }

    @Override
    public DataComponent<PotionType> potionType() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, PotionType potionType) {
                ItemStack itemStack = (ItemStack) item;
                if (potionType == null) {
                    itemStack.remove(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
                    return;
                }

                itemStack.set(net.minecraft.core.component.DataComponents.POTION_CONTENTS, new PotionContents(CraftPotionType.bukkitToMinecraftHolder(potionType)));
            }

            @Override
            public PotionType get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                PotionContents pot = itemStack.get(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
                if (pot == null || pot.potion().isEmpty()) {
                    return PotionType.AWKWARD;
                }

                return CraftPotionType.minecraftHolderToBukkit(pot.potion().get());
            }
        };
    }
}
