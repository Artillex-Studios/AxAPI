package com.artillexstudios.axapi.gui.inventory.builder;

import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.implementation.DynamicGui;

public class DynamicGuiBuilder extends GuiBuilder<DynamicGui> {

    @Override
    public DynamicGui build() {
        return new DynamicGui(this.titleProvider, this.type, this.rows);
    }
}
