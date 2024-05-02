package com.artillexstudios.axapi.nms.v1_20_R4.items;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.items.component.DyedColor;
import com.artillexstudios.axapi.items.component.ProfileProperties;
import com.artillexstudios.axapi.nms.v1_20_R4.ItemStackSerializer;
import com.artillexstudios.axapi.nms.v1_20_R4.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class WrappedItemStack implements com.artillexstudios.axapi.items.WrappedItemStack {
    private net.minecraft.world.item.ItemStack itemStack;
    private ItemStack bukkitStack;

    public WrappedItemStack(ItemStack itemStack) {
        this(itemStack instanceof CraftItemStack cr ? cr.handle : CraftItemStack.asNMSCopy(itemStack));
        this.bukkitStack = itemStack;
    }

    public WrappedItemStack(net.minecraft.world.item.ItemStack itemStack) {
        this.itemStack = itemStack;
        this.bukkitStack = null;
    }

    @Override
    public <T> void set(DataComponent<T> component, T value) {
        if (component == DataComponent.CUSTOM_DATA) {
            if (value == null) {
                itemStack.remove(DataComponents.CUSTOM_DATA);
                return;
            }

            var tag = ((CompoundTag) value);
            if (tag.getParent().isEmpty()) {
                itemStack.set(DataComponents.CUSTOM_DATA, null);
                return;
            }

            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag.getParent()));
        } else if (component == DataComponent.MAX_STACK_SIZE) {
            if (value == null) {
                itemStack.remove(DataComponents.MAX_STACK_SIZE);
                return;
            }

            itemStack.set(DataComponents.MAX_STACK_SIZE, (Integer) value);
        } else if (component == DataComponent.MAX_DAMAGE) {
            if (value == null) {
                itemStack.remove(DataComponents.MAX_DAMAGE);
                return;
            }

            itemStack.set(DataComponents.MAX_DAMAGE, (Integer) value);
        } else if (component == DataComponent.DAMAGE) {
            if (value == null) {
                itemStack.remove(DataComponents.DAMAGE);
                return;
            }

            itemStack.set(DataComponents.DAMAGE, (Integer) value);
        } else if (component == DataComponent.UNBREAKABLE) {
            if (value == null) {
                itemStack.remove(DataComponents.UNBREAKABLE);
                return;
            }

            itemStack.set(DataComponents.UNBREAKABLE, new Unbreakable(((com.artillexstudios.axapi.items.component.Unbreakable) value).showInTooltip()));
        } else if (component == DataComponent.CUSTOM_NAME) {
            if (value == null) {
                itemStack.remove(DataComponents.CUSTOM_NAME);
                return;
            }

            itemStack.set(DataComponents.CUSTOM_NAME, ComponentSerializer.INSTANCE.toVanilla((Component) value));
        } else if (component == DataComponent.ITEM_NAME) {
            if (value == null) {
                itemStack.remove(DataComponents.ITEM_NAME);
                return;
            }

            itemStack.set(DataComponents.ITEM_NAME, ComponentSerializer.INSTANCE.toVanilla((Component) value));
        } else if (component == DataComponent.LORE) {
            if (value == null) {
                itemStack.remove(DataComponents.LORE);
                return;
            }

            var lore = (com.artillexstudios.axapi.items.component.ItemLore) value;
            itemStack.set(DataComponents.LORE, new ItemLore(ComponentSerializer.INSTANCE.toVanillaList(lore.lines()), ComponentSerializer.INSTANCE.toVanillaList(lore.styledLines())));
        } else if (component == DataComponent.RARITY) {
            if (value == null) {
                itemStack.remove(DataComponents.RARITY);
                return;
            }

            itemStack.set(DataComponents.RARITY, Rarity.valueOf(((com.artillexstudios.axapi.items.component.Rarity) value).name()));
        } else if (component == DataComponent.ENCHANTMENTS) {
            if (value == null) {
                itemStack.remove(DataComponents.ENCHANTMENTS);
                return;
            }

            var itemEnchantments = (com.artillexstudios.axapi.items.component.ItemEnchantments) value;
            var vanilla = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            vanilla.showInTooltip = itemEnchantments.showInTooltip();

            for (Map.Entry<Enchantment, Integer> e : itemEnchantments.entrySet()) {
                vanilla.set(CraftEnchantment.bukkitToMinecraft(e.getKey()), e.getValue());
            }

            itemStack.set(DataComponents.ENCHANTMENTS, vanilla.toImmutable());
        } else if (component == DataComponent.CUSTOM_MODEL_DATA) {
            if (value == null) {
                itemStack.remove(DataComponents.CUSTOM_MODEL_DATA);
                return;
            }

            itemStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData((Integer) value));
        } else if (component == DataComponent.HIDE_ADDITIONAL_TOOLTIP) {
            if (value == null) {
                itemStack.remove(DataComponents.HIDE_ADDITIONAL_TOOLTIP);
                return;
            }

            itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        } else if (component == DataComponent.HIDE_TOOLTIP) {
            if (value == null) {
                itemStack.remove(DataComponents.HIDE_TOOLTIP);
                return;
            }

            itemStack.set(DataComponents.HIDE_TOOLTIP, Unit.INSTANCE);
        } else if (component == DataComponent.REPAIR_COST) {
            if (value == null) {
                itemStack.remove(DataComponents.REPAIR_COST);
                return;
            }

            itemStack.set(DataComponents.REPAIR_COST, (Integer) value);
        } else if (component == DataComponent.CREATIVE_SLOT_LOCK) {
            if (value == null) {
                itemStack.remove(DataComponents.CREATIVE_SLOT_LOCK);
                return;
            }

            itemStack.set(DataComponents.CREATIVE_SLOT_LOCK, Unit.INSTANCE);
        } else if (component == DataComponent.ENCHANTMENT_GLINT_OVERRIDE) {
            if (value == null) {
                itemStack.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
                return;
            }

            itemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, (Boolean) value);
        } else if (component == DataComponent.INTANGIBLE_PROJECTILE) {
            if (value == null) {
                itemStack.remove(DataComponents.INTANGIBLE_PROJECTILE);
                return;
            }

            itemStack.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
        } else if (component == DataComponent.STORED_ENCHANTMENTS) {
            if (value == null) {
                itemStack.remove(DataComponents.STORED_ENCHANTMENTS);
                return;
            }

            var itemEnchantments = (com.artillexstudios.axapi.items.component.ItemEnchantments) value;
            var vanilla = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            vanilla.showInTooltip = itemEnchantments.showInTooltip();

            for (Map.Entry<Enchantment, Integer> e : itemEnchantments.entrySet()) {
                vanilla.set(CraftEnchantment.bukkitToMinecraft(e.getKey()), e.getValue());
            }

            itemStack.set(DataComponents.STORED_ENCHANTMENTS, vanilla.toImmutable());
        } else if (component == DataComponent.PROFILE) {
            if (value == null) {
                itemStack.remove(DataComponents.PROFILE);
                return;
            }

            var profileProperties = (ProfileProperties) value;
            var gameProfile = new GameProfile(profileProperties.uuid(), profileProperties.name());

            for (Map.Entry<String, ProfileProperties.Property> entry : profileProperties.properties().entries()) {
                var property = entry.getValue();
                gameProfile.getProperties().put(entry.getKey(), new Property(property.name(), property.value(), property.signature()));
            }

            itemStack.set(DataComponents.PROFILE, new ResolvableProfile(gameProfile));
        } else if (component == DataComponent.MATERIAL) {
            DataComponentPatch patch = itemStack.getComponentsPatch();
            itemStack.setItem(CraftItemType.bukkitToMinecraft((Material) value));
            itemStack.restorePatch(patch);
        } else if (component == DataComponent.DYED_COLOR) {
            if (value == null) {
                itemStack.remove(DataComponents.DYED_COLOR);
                return;
            }

            var color = (DyedColor) value;
            itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.rgb(), color.showInTooltip()));
        } else if (component == DataComponent.POTION_CONTENTS) {
            if (value == null) {
                itemStack.remove(DataComponents.POTION_CONTENTS);
                return;
            }

            itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(CraftPotionType.bukkitToMinecraftHolder((PotionType) value)));
        }
    }

    @Override
    public <T> T get(DataComponent<T> component) {
        if (component == DataComponent.CUSTOM_DATA) {
            return (T) new CompoundTag(itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.of(new net.minecraft.nbt.CompoundTag())).copyTag());
        } else if (component == DataComponent.MAX_STACK_SIZE) {
            return (T) itemStack.getOrDefault(DataComponents.MAX_STACK_SIZE, 0);
        } else if (component == DataComponent.MAX_DAMAGE) {
            return (T) itemStack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
        } else if (component == DataComponent.DAMAGE) {
            return (T) itemStack.getOrDefault(DataComponents.DAMAGE, 0);
        } else if (component == DataComponent.UNBREAKABLE) {
            var vanilla = itemStack.get(DataComponents.UNBREAKABLE);
            var unbreakable = new com.artillexstudios.axapi.items.component.Unbreakable(vanilla != null && vanilla.showInTooltip());
            return (T) unbreakable;
        } else if (component == DataComponent.CUSTOM_NAME) {
            var chatComponent = itemStack.get(DataComponents.CUSTOM_NAME);
            return (T) (chatComponent != null ? ComponentSerializer.INSTANCE.fromVanilla(chatComponent) : Component.empty());
        } else if (component == DataComponent.ITEM_NAME) {
            var chatComponent = itemStack.get(DataComponents.ITEM_NAME);
            return (T) (chatComponent != null ? ComponentSerializer.INSTANCE.fromVanilla(chatComponent) : Component.empty());
        } else if (component == DataComponent.LORE) {
            var lore = itemStack.get(DataComponents.LORE);
            if (lore == null) {
                return (T) new com.artillexstudios.axapi.items.component.ItemLore(List.of(), List.of());
            }

            List<Object> lines = new ArrayList<>(lore.lines().size());
            List<Object> styled = new ArrayList<>(lore.styledLines().size());

            lines.addAll(lore.lines());
            styled.addAll(lore.styledLines());

            return (T) new com.artillexstudios.axapi.items.component.ItemLore(ComponentSerializer.INSTANCE.fromVanillaList(lines), ComponentSerializer.INSTANCE.fromVanillaList(styled));
        } else if (component == DataComponent.RARITY) {

            return (T) com.artillexstudios.axapi.items.component.Rarity.valueOf(itemStack.getOrDefault(DataComponents.RARITY, Rarity.COMMON).name());
        } else if (component == DataComponent.ENCHANTMENTS) {
            var vanilla = itemStack.get(DataComponents.ENCHANTMENTS);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            if (vanilla == null) {
                return (T) new com.artillexstudios.axapi.items.component.ItemEnchantments(enchants, true);
            }

            vanilla.entrySet().forEach(e -> {
                enchants.put(CraftEnchantment.minecraftToBukkit(e.getKey().value()), e.getIntValue());
            });

            return (T) new com.artillexstudios.axapi.items.component.ItemEnchantments(enchants, vanilla.showInTooltip);
        } else if (component == DataComponent.CUSTOM_MODEL_DATA) {
            return (T) Integer.valueOf(itemStack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value());
        } else if (component == DataComponent.HIDE_ADDITIONAL_TOOLTIP) {
            return itemStack.get(DataComponents.HIDE_ADDITIONAL_TOOLTIP) == null ? null : (T) com.artillexstudios.axapi.items.component.Unit.INSTANCE;
        } else if (component == DataComponent.HIDE_TOOLTIP) {
            return itemStack.get(DataComponents.HIDE_TOOLTIP) == null ? null : (T) com.artillexstudios.axapi.items.component.Unit.INSTANCE;
        } else if (component == DataComponent.REPAIR_COST) {
            return (T) itemStack.getOrDefault(DataComponents.REPAIR_COST, 0);
        } else if (component == DataComponent.CREATIVE_SLOT_LOCK) {
            return itemStack.get(DataComponents.CREATIVE_SLOT_LOCK) == null ? null : (T) com.artillexstudios.axapi.items.component.Unit.INSTANCE;
        } else if (component == DataComponent.ENCHANTMENT_GLINT_OVERRIDE) {
            return (T) itemStack.getOrDefault(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        } else if (component == DataComponent.INTANGIBLE_PROJECTILE) {
            return itemStack.get(DataComponents.INTANGIBLE_PROJECTILE) == null ? null : (T) com.artillexstudios.axapi.items.component.Unit.INSTANCE;
        } else if (component == DataComponent.STORED_ENCHANTMENTS) {
            var vanilla = itemStack.get(DataComponents.STORED_ENCHANTMENTS);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            if (vanilla == null) {
                return (T) new com.artillexstudios.axapi.items.component.ItemEnchantments(enchants, false);
            }

            vanilla.entrySet().forEach(e -> {
                enchants.put(CraftEnchantment.minecraftToBukkit(e.getKey().value()), e.getIntValue());
            });

            return (T) new com.artillexstudios.axapi.items.component.ItemEnchantments(enchants, vanilla.showInTooltip);
        } else if (component == DataComponent.PROFILE) {
            var profile = itemStack.get(DataComponents.PROFILE);
            var profileProperties = new ProfileProperties(profile.id().orElse(new UUID(0, 0)), profile.name().orElse(""));
            profile.properties().forEach((k, v) -> {
                profileProperties.put(k, new ProfileProperties.Property(v.name(), v.value(), v.signature()));
            });
            return (T) profileProperties;
        } else if (component == DataComponent.MATERIAL) {
            return (T) CraftMagicNumbers.getMaterial(itemStack.getItem());
        } else if (component == DataComponent.DYED_COLOR) {
            var color = itemStack.get(DataComponents.DYED_COLOR);
            if (color == null) {
                return (T) new DyedColor(Color.fromRGB(0), false);
            }
            return (T) new DyedColor(Color.fromRGB(color.rgb()), color.showInTooltip());
        } else if (component == DataComponent.POTION_CONTENTS) {
            var pot = itemStack.get(DataComponents.POTION_CONTENTS);
            if (pot == null || pot.potion().isEmpty()) {
                return (T) PotionType.AWKWARD;
            }

            return (T) CraftPotionType.minecraftHolderToBukkit(pot.potion().get());
        }

        throw new RuntimeException("Unhandled DataComponent " + component.getClass() + " !");
    }

    @Override
    public int getAmount() {
        return itemStack.getCount();
    }

    @Override
    public void setAmount(int amount) {
        itemStack.setCount(amount);
    }

    @Override
    public ItemStack toBukkit() {
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public String toSNBT() {
        var compoundTag = (net.minecraft.nbt.CompoundTag) itemStack.save(MinecraftServer.getServer().registryAccess());
        compoundTag.putInt("DataVersion", CraftMagicNumbers.INSTANCE.getDataVersion());
        return new SnbtPrinterTagVisitor().visit(compoundTag);
    }

    @Override
    public byte[] serialize() {
        return ItemStackSerializer.INSTANCE.serializeAsBytes(toBukkit());
    }

    @Override
    public void finishEdit() {
        ItemMeta meta = CraftItemStack.getItemMeta(itemStack);
        if (bukkitStack != null) {
            bukkitStack.setItemMeta(meta);
        } else {
            CraftItemStack.setItemMeta(itemStack, meta);
        }
    }
}
