package com.artillexstudios.axapi.gui.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public abstract class Gui {
    private final InventoryType type;
    private Component title;
    private int rows;

    public Gui(Component title, InventoryType type, int rows) {
        this.title = title;
        this.type = type;
        this.rows = rows;
    }

    public void setItem(int slot, GuiItem item) {

    }

    public int rows() {
        return this.rows;
    }

    public Component title() {
        return this.title;
    }

    public void title(Component title) {
        this.title = title;
    }

    public InventoryType type() {
        return this.type;
    }

    public void open(Player player) {

    }
}
