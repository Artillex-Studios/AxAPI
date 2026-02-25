package com.artillexstudios.axapi.gui.inventory.listener;

import com.artillexstudios.axapi.gui.inventory.renderer.InventoryRenderer;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.PaperUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getClickedInventory() != null && event.getClickedInventory().equals(event.getView().getBottomInventory())) {
            this.checkAndRemove(event.getCurrentItem());
        }

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
        this.checkAndRemove(event.getPlayer().getInventory().getContents());
        if (!(PaperUtils.getHolder(event.getInventory(), false) instanceof InventoryRenderer renderer)) {
            return;
        }

        renderer.handleClose(event);
    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        this.checkAndRemove(event.getPlayer().getInventory().getContents());
        if (!(PaperUtils.getHolder(event.getInventory(), false) instanceof InventoryRenderer renderer)) {
            return;
        }

        renderer.handleOpen(event);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        this.checkAndRemove(event.getItemDrop().getItemStack());
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        this.checkAndRemove(event.getItem());
    }

    private void checkAndRemove(ItemStack[] items) {
        for (ItemStack item : items) {
            this.checkAndRemove(item);
        }
    }

    private void checkAndRemove(ItemStack stack) {
        if (stack == null) {
            return;
        }

        WrappedItemStack wrapped = WrappedItemStack.wrap(stack);
        CompoundTag compoundTag = wrapped.get(DataComponents.customData());
        if (!compoundTag.contains("axapi-gui")) {
            return;
        }

        stack.setAmount(0);
    }
}
