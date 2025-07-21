package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.modifier.WrappedItemStackModifier;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;
import java.util.function.Function;

public class DynamicGui extends Gui {

    public DynamicGui(Player player, Function<HashMapContext, Component> titleProvider, InventoryType type, int rows, HashMapContext context, List<WrappedItemStackModifier> modifiers) {
        super(player, titleProvider, type, rows, context, modifiers);
    }
}
