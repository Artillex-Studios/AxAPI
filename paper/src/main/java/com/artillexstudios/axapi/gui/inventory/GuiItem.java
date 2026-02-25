package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record GuiItem(Function<HashMapContext, WrappedItemStack> stack,
                      BiConsumer<HashMapContext, InventoryClickEvent> eventConsumer) {

    public GuiItem(Function<HashMapContext, WrappedItemStack> stack) {
        this(stack, (ctx, event) -> {});
    }
}
