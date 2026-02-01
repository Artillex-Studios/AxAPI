package com.artillexstudios.axapi.nms.v1_21_R7.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.ItemLore;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public final class LoreDataComponent implements DataComponentHandler<ItemLore, net.minecraft.world.item.component.ItemLore> {

    @Override
    public net.minecraft.world.item.component.ItemLore toNMS(ItemLore from) {
        return switch (from) {
            case ItemLore.Unstyled(List<Component> lines) ->
                    new net.minecraft.world.item.component.ItemLore(ComponentSerializer.INSTANCE.toVanillaList(lines));
            case ItemLore.Styled(List<Component> lines, List<Component> styled) ->
                    new net.minecraft.world.item.component.ItemLore(ComponentSerializer.INSTANCE.toVanillaList(lines), ComponentSerializer.INSTANCE.toVanillaList(styled));
        };
    }

    @Override
    public ItemLore fromNMS(net.minecraft.world.item.component.ItemLore data) {
        return new ItemLore.Styled(ComponentSerializer.INSTANCE.fromVanillaList(new ArrayList<>(data.lines())), ComponentSerializer.INSTANCE.fromVanillaList(new ArrayList<>(data.styledLines())));
    }
}
