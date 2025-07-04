package com.artillexstudios.axapi.gui.inventory.builder;

import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.implementation.StaticGui;
import org.bukkit.entity.Player;

public class StaticGuiBuilder extends GuiBuilder<StaticGui> {

    // TODO: create a single instance, and return that
    @Override
    public StaticGui build(Player player) {
        return new StaticGui(player, this.titleProvider, this.type, this.rows);
    }
}
