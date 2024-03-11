package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface ActionBar {

    static ActionBar create(Component title) {
        return NMSHandlers.getNmsHandler().newActionBar(title);
    }

    static void send(Player player, Component title) {
        ActionBar bar = NMSHandlers.getNmsHandler().newActionBar(title);
        bar.send(player);
    }

    void setContent(Component component);

    void send(Player player);
}
