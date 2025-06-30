package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public record GuiItem(WrappedItemStack stack, Consumer<InventoryClickEvent> eventConsumer) {

    public GuiItem(WrappedItemStack stack) {
        this(stack, event -> {});
    }
}
