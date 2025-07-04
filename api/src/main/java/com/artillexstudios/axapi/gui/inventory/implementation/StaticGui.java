package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.Function;

public class StaticGui extends Gui {

    public StaticGui(Player player, Function<HashMapContext, Component> titleProvider, InventoryType type, int rows) {
        super(player, titleProvider, type, rows);
    }
}
