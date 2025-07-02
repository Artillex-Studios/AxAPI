package com.artillexstudios.axapi.gui.inventory.listener;

import com.artillexstudios.axapi.gui.inventory.InventoryRenderer;
import com.artillexstudios.axapi.utils.PaperUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public final class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(PaperUtils.getHolder(event.getInventory(), false) instanceof InventoryRenderer renderer)) {
            return;
        }

        renderer.handleClick(event);
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        if (!(PaperUtils.getHolder(event.getInventory(), false) instanceof InventoryRenderer renderer)) {
            return;
        }

        renderer.handleDrag(event);
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (!(PaperUtils.getHolder(event.getInventory(), false) instanceof InventoryRenderer renderer)) {
            return;
        }

        renderer.handleClose(event);
    }
}
