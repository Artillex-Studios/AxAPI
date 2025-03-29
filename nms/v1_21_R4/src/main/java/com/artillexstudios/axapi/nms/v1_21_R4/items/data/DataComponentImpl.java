package com.artillexstudios.axapi.nms.v1_21_R4.items.data;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.items.component.type.CustomModelData;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
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
import java.util.UUID;

public class DataComponentImpl implements com.artillexstudios.axapi.items.component.DataComponentImpl {

    @Override
    public DataComponent<CompoundTag> customData() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, CompoundTag compoundTag) {
                ItemStack itemStack = (ItemStack) item;

                if (compoundTag == null || compoundTag.isEmpty()) {
                    itemStack.remove(DataComponents.CUSTOM_DATA);
                    return;
                }

                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of((net.minecraft.nbt.CompoundTag) compoundTag.getParent()));
            }

            @Override
            public CompoundTag get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return new com.artillexstudios.axapi.nms.v1_21_R4.items.nbt.CompoundTag(itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.of(new net.minecraft.nbt.CompoundTag())).copyTag());
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
                    itemStack.remove(DataComponents.MAX_STACK_SIZE);
                    return;
                }

                itemStack.set(DataComponents.MAX_STACK_SIZE, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return itemStack.getOrDefault(DataComponents.MAX_STACK_SIZE, 0);
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
                    itemStack.remove(DataComponents.MAX_DAMAGE);
                    return;
                }

                itemStack.set(DataComponents.MAX_DAMAGE, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return itemStack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
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
                    itemStack.remove(DataComponents.DAMAGE);
                    return;
                }

                itemStack.set(DataComponents.DAMAGE, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                return itemStack.getOrDefault(DataComponents.DAMAGE, 0);
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
                    itemStack.remove(DataComponents.UNBREAKABLE);
                    return;
                }

                itemStack.set(DataComponents.UNBREAKABLE, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unbreakable get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.util.Unit vanilla = itemStack.get(DataComponents.UNBREAKABLE);
                return new Unbreakable(true);
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
                    itemStack.remove(DataComponents.CUSTOM_NAME);
                    return;
                }

                itemStack.set(DataComponents.CUSTOM_NAME, ComponentSerializer.instance().toVanilla(component));
            }

            @Override
            public Component get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.network.chat.Component component = itemStack.get(DataComponents.CUSTOM_NAME);
                return component == null ? Component.empty() : ComponentSerializer.instance().fromVanilla(component);
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
                    itemStack.remove(DataComponents.ITEM_NAME);
                    return;
                }

                itemStack.set(DataComponents.ITEM_NAME, ComponentSerializer.instance().toVanilla(component));
            }

            @Override
            public Component get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.network.chat.Component component = itemStack.get(DataComponents.ITEM_NAME);
                return component == null ? Component.empty() : ComponentSerializer.instance().fromVanilla(component);
            }
        };
    }

    @Override
    public DataComponent<Key> itemModel() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Key key) {
                ItemStack itemStack = (ItemStack) item;
                if (key == null) {
                    itemStack.remove(DataComponents.ITEM_MODEL);
                    return;
                }

                itemStack.set(DataComponents.ITEM_MODEL, ResourceLocation.fromNamespaceAndPath(key.namespace(), key.value()));
            }

            @Override
            public Key get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                ResourceLocation resourceLocation = itemStack.get(DataComponents.ITEM_MODEL);
                return resourceLocation == null ? null : Key.key(resourceLocation.getNamespace(), resourceLocation.getPath());
            }
        };
    }

    @Override
    public DataComponent<ItemLore> lore() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, ItemLore itemLore) {
                ItemStack itemStack = (ItemStack) item;
                if (itemLore == null) {
                    itemStack.remove(DataComponents.LORE);
                    return;
                }

                itemStack.set(DataComponents.LORE, new net.minecraft.world.item.component.ItemLore(ComponentSerializer.instance().toVanillaList(itemLore.lines()), ComponentSerializer.instance().toVanillaList(itemLore.styledLines())));
            }

            @Override
            public ItemLore get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.world.item.component.ItemLore lore = itemStack.get(DataComponents.LORE);
                return new ItemLore(lore == null ? List.of() : ComponentSerializer.instance().fromVanillaList(new ArrayList<>(lore.lines())), lore == null ? List.of() : ComponentSerializer.instance().fromVanillaList(new ArrayList<>(lore.styledLines())));
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
                    itemStack.remove(DataComponents.RARITY);
                    return;
                }

                itemStack.set(DataComponents.RARITY, net.minecraft.world.item.Rarity.valueOf(rarity.name()));
            }

            @Override
            public Rarity get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return Rarity.valueOf(itemStack.getOrDefault(DataComponents.RARITY, net.minecraft.world.item.Rarity.COMMON).name());
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
                    itemStack.remove(DataComponents.ENCHANTMENTS);
                    return;
                }

                net.minecraft.world.item.enchantment.ItemEnchantments.Mutable vanilla = new net.minecraft.world.item.enchantment.ItemEnchantments.Mutable(net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
//                vanilla.showInTooltip = itemEnchantments.showInTooltip();

                for (Map.Entry<Enchantment, Integer> e : itemEnchantments.entrySet()) {
                    vanilla.set(CraftEnchantment.bukkitToMinecraftHolder(e.getKey()), e.getValue());
                }

                itemStack.set(DataComponents.ENCHANTMENTS, vanilla.toImmutable());
            }

            @Override
            public ItemEnchantments get(Object item) {
                ItemStack itemStack = (ItemStack) item;

                net.minecraft.world.item.enchantment.ItemEnchantments vanilla = itemStack.get(DataComponents.ENCHANTMENTS);
                HashMap<Enchantment, Integer> enchants = new HashMap<>();
                if (vanilla == null) {
                    return new ItemEnchantments(enchants, true);
                }

                vanilla.entrySet().forEach(e -> enchants.put(CraftEnchantment.minecraftToBukkit(e.getKey().value()), e.getIntValue()));

                return new ItemEnchantments(enchants, true/*vanilla.showInTooltip*/);
            }
        };
    }

    @Override
    public DataComponent<CustomModelData> customModelData() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, CustomModelData modelData) {
                ItemStack itemStack = (ItemStack) item;
                if (modelData == null) {
                    itemStack.remove(DataComponents.CUSTOM_MODEL_DATA);
                    return;
                }

                itemStack.set(DataComponents.CUSTOM_MODEL_DATA, new net.minecraft.world.item.component.CustomModelData(modelData.floats(), modelData.flags(), modelData.strings(), modelData.colors()));
            }

            @Override
            public CustomModelData get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.world.item.component.CustomModelData data = itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
                if (data == null) {
                    return null;
                }

                return new CustomModelData(data.strings(), data.flags(), data.floats(), data.colors());
            }
        };
    }

    @Override
    public DataComponent<Unit> hideAdditionalTooltip() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
//                ItemStack itemStack = (ItemStack) item;
//                if (unit == null) {
//                    itemStack.remove(DataComponents.HIDE_ADDITIONAL_TOOLTIP);
//                    return;
//                }
//
//                itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
//                ItemStack itemStack = (ItemStack) item;
//                return itemStack.get(DataComponents.HIDE_ADDITIONAL_TOOLTIP) == null ? null : Unit.INSTANCE;
                return null;
            }
        };
    }

    @Override
    public DataComponent<Unit> hideTooltip() {
        return new DataComponent<>() {

            @Override
            public void apply(Object item, Unit unit) {
//                ItemStack itemStack = (ItemStack) item;
//                if (unit == null) {
//                    itemStack.remove(DataComponents.HIDE_TOOLTIP);
//                    return;
//                }
//
//                itemStack.set(DataComponents.HIDE_TOOLTIP, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
//                ItemStack itemStack = (ItemStack) item;
//                return itemStack.get(DataComponents.HIDE_TOOLTIP) == null ? null : Unit.INSTANCE;
                return null;
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
                    itemStack.remove(DataComponents.REPAIR_COST);
                    return;
                }

                itemStack.set(DataComponents.REPAIR_COST, integer);
            }

            @Override
            public Integer get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.getOrDefault(DataComponents.REPAIR_COST, 0);
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
                    itemStack.remove(DataComponents.CREATIVE_SLOT_LOCK);
                    return;
                }

                itemStack.set(DataComponents.CREATIVE_SLOT_LOCK, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.get(DataComponents.CREATIVE_SLOT_LOCK) == null ? null : Unit.INSTANCE;
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
                    itemStack.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
                    return;
                }

                itemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, glint);
            }

            @Override
            public Boolean get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.getOrDefault(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
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
                    itemStack.remove(DataComponents.INTANGIBLE_PROJECTILE);
                    return;
                }

                itemStack.set(DataComponents.INTANGIBLE_PROJECTILE, net.minecraft.util.Unit.INSTANCE);
            }

            @Override
            public Unit get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                return itemStack.get(DataComponents.INTANGIBLE_PROJECTILE) == null ? null : Unit.INSTANCE;
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
                    itemStack.remove(DataComponents.STORED_ENCHANTMENTS);
                    return;
                }

                net.minecraft.world.item.enchantment.ItemEnchantments.Mutable vanilla = new net.minecraft.world.item.enchantment.ItemEnchantments.Mutable(net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
//                vanilla.showInTooltip = itemEnchantments.showInTooltip();

                for (Map.Entry<Enchantment, Integer> e : itemEnchantments.entrySet()) {
                    vanilla.set(CraftEnchantment.bukkitToMinecraftHolder(e.getKey()), e.getValue());
                }

                itemStack.set(DataComponents.STORED_ENCHANTMENTS, vanilla.toImmutable());
            }

            @Override
            public ItemEnchantments get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                net.minecraft.world.item.enchantment.ItemEnchantments vanilla = itemStack.get(DataComponents.STORED_ENCHANTMENTS);

                HashMap<Enchantment, Integer> enchants = new HashMap<>();
                if (vanilla == null) {
                    return new ItemEnchantments(enchants, true);
                }

                vanilla.entrySet().forEach(e -> enchants.put(CraftEnchantment.minecraftToBukkit(e.getKey().value()), e.getIntValue()));

                return new ItemEnchantments(enchants, true/*vanilla.showInTooltip*/);
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
                    itemStack.remove(DataComponents.PROFILE);
                    return;
                }

                GameProfile gameProfile = new GameProfile(profileProperties.uuid(), profileProperties.name());

                for (Map.Entry<String, ProfileProperties.Property> entry : profileProperties.properties().entries()) {
                    var property = entry.getValue();
                    gameProfile.getProperties().put(entry.getKey(), new Property(property.name(), property.value(), property.signature()));
                }

                itemStack.set(DataComponents.PROFILE, new ResolvableProfile(gameProfile));
            }

            @Override
            public ProfileProperties get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                ResolvableProfile profile = itemStack.get(DataComponents.PROFILE);

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
                    itemStack.remove(DataComponents.DYED_COLOR);
                    return;
                }

//                itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(dyedColor.rgb()/*, dyedColor.showInTooltip()*/));
            }

            @Override
            public DyedColor get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                var color = itemStack.get(DataComponents.DYED_COLOR);
                return new DyedColor(color == null ? Color.fromRGB(0) : Color.fromRGB(color.rgb()), color == null ? true : true/*color.showInTooltip()*/);
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
                    itemStack.remove(DataComponents.POTION_CONTENTS);
                    return;
                }

                itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(CraftPotionType.bukkitToMinecraftHolder(potionType)));
            }

            @Override
            public PotionType get(Object item) {
                ItemStack itemStack = (ItemStack) item;
                PotionContents pot = itemStack.get(DataComponents.POTION_CONTENTS);
                if (pot == null || pot.potion().isEmpty()) {
                    return PotionType.AWKWARD;
                }

                return CraftPotionType.minecraftHolderToBukkit(pot.potion().get());
            }
        };
    }
}
