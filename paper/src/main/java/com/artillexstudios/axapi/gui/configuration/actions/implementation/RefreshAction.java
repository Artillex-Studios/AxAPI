package com.artillexstudios.axapi.gui.configuration.actions.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.configuration.actions.Action;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiKeys;
import org.bukkit.entity.Player;

public final class RefreshAction extends Action<String> {

    public RefreshAction(String data) {
        super(data);
    }

    @Override
    public String transform(String data) {
        return "";
    }

    @Override
    public void run(Player player, HashMapContext context) {
        Gui gui = context.get(GuiKeys.GUI);
        gui.open();
    }
}
