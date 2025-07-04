package com.artillexstudios.axapi.gui.inventory.builder;

import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.implementation.DynamicGui;
import org.bukkit.entity.Player;

public class DynamicGuiBuilder extends GuiBuilder<DynamicGui> {

    @Override
    public DynamicGui build(Player player) {
        return new DynamicGui(player, this.titleProvider, this.type, this.rows);
    }
}
