package com.artillexstudios.axapi.gui.configuration.actions.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.configuration.actions.Action;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiKeys;
import com.artillexstudios.axapi.placeholders.PlaceholderHandler;
import com.artillexstudios.axapi.placeholders.PlaceholderParameters;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ConsoleCommandAction extends Action<String> {

    public ConsoleCommandAction(String data) {
        super(data);
    }

    @Override
    public String transform(String data) {
        return data;
    }

    @Override
    public void run(Player player, HashMapContext context) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderHandler.parseWithPlaceholderAPI(this.value(), new PlaceholderParameters()
                .withParameters(Player.class, player)
                .withParameters(Gui.class, context.get(GuiKeys.GUI))
        ));
    }
}
