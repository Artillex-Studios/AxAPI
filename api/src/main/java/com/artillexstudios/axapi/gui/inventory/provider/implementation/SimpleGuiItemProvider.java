package com.artillexstudios.axapi.gui.inventory.provider.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;

import java.util.concurrent.CompletableFuture;

public class SimpleGuiItemProvider implements GuiItemProvider {
    private final GuiItem item;

    public SimpleGuiItemProvider(GuiItem item) {
        this.item = item;
    }

    @Override
    public CompletableFuture<GuiItem> provide(HashMapContext context) {
        return CompletableFuture.completedFuture(this.item);
    }
}
