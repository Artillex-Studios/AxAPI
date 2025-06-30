package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.gui.inventory.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

public class PaginatedGui extends Gui {

    public PaginatedGui(Component title, InventoryType type, int rows) {
        super(title, type, rows);
    }
}
