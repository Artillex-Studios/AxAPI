package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.gui.inventory.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

public class StaticGui extends Gui {

    public StaticGui(Component title, InventoryType type, int rows) {
        super(title, type, rows);
    }
}
