package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record GuiItem(Supplier<WrappedItemStack> stack, Consumer<InventoryClickEvent> eventConsumer) {

    public GuiItem(Supplier<WrappedItemStack> stack) {
        this(stack, event -> {});
    }
}
