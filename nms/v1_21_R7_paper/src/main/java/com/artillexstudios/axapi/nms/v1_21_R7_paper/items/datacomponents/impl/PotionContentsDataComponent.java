package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.PotionContents;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PotionContentsDataComponent implements DataComponentHandler<PotionContents, net.minecraft.world.item.alchemy.PotionContents> {

    @Override
    public net.minecraft.world.item.alchemy.PotionContents toNMS(PotionContents from) {
        Optional<Holder<@NotNull Potion>> potionHolder = from.type().isEmpty() ? Optional.empty() : Optional.of(CraftPotionType.bukkitToMinecraftHolder(from.type().get()));
        List<MobEffectInstance> effects = new ArrayList<>(from.effects().size());
        for (PotionEffect effect : from.effects()) {
            effects.add(CraftPotionUtil.fromBukkit(effect));
        }

        return new net.minecraft.world.item.alchemy.PotionContents(potionHolder, from.color(), effects, from.name());
    }

    @Override
    public PotionContents fromNMS(net.minecraft.world.item.alchemy.PotionContents data) {
        Optional<PotionType> type = data.potion().isEmpty() ? Optional.empty() : Optional.of(CraftPotionType.minecraftHolderToBukkit(data.potion().get()));
        List<PotionEffect> types = new ArrayList<>(data.customEffects().size());
        for (MobEffectInstance effectInstance : data.customEffects()) {
            types.add(CraftPotionUtil.toBukkit(effectInstance));
        }

        return new PotionContents(type, data.customColor(), types, data.customName());
    }
}
