package com.artillexstudios.axapi.gui.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public record BakedGuiItem(ItemStack stack, Consumer<InventoryClickEvent> eventConsumer) {
}
