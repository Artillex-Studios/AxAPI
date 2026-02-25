package com.artillexstudios.axapi.gui.configuration.actions.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.configuration.actions.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class CloseAction extends Action<Object> {

    public CloseAction(String data) {
        super(data);
    }

    @Override
    public Object transform(String data) {
        return "";
    }

    @Override
    public void run(Player player, HashMapContext context) {
        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
    }
}
