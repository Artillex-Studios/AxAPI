package com.artillexstudios.axapi.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public final class AnvilListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getType() != InventoryType.ANVIL) {
            return;
        }

        AnvilInput input = AnvilInput.get(player);
        if (input == null) {
            return;
        }

        input.event().accept(event);
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getType() != InventoryType.ANVIL) {
            return;
        }

        AnvilInput input = AnvilInput.remove(player);
        if (input == null) {
            return;
        }

        input.closeEvent().accept(event);
    }
}
