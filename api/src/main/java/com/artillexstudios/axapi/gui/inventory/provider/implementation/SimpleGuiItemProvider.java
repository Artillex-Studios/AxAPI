package com.artillexstudios.axapi.gui.inventory.provider.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;

import java.util.concurrent.CompletableFuture;

public class SimpleGuiItemProvider implements GuiItemProvider {
    private final GuiItem item;

    public SimpleGuiItemProvider(GuiItem item) {
        this.item = item;
    }

    @Override
    public CompletableFuture<BakedGuiItem> provide(HashMapContext context) {
        return CompletableFuture.completedFuture(new BakedGuiItem(this.item.stack().apply(context).toBukkit(), event -> this.item.eventConsumer().accept(context, event)));
    }
}
