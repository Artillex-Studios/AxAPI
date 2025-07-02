package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.Function;

public class PaginatedGui extends Gui {

    public PaginatedGui(Function<HashMapContext, Component> titleProvider, InventoryType type, int rows) {
        super(titleProvider, type, rows);
    }
}
