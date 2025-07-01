package com.artillexstudios.axapi.gui.inventory.provider.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;

import java.util.concurrent.CompletableFuture;

public class CachingGuiItemProvider implements GuiItemProvider {
    private final GuiItem guiItem;
    private CompletableFuture<BakedGuiItem> future;

    public CachingGuiItemProvider(GuiItem guiItem) {
        this.guiItem = guiItem;
    }

    @Override
    public CompletableFuture<BakedGuiItem> provide(HashMapContext context) {
        if (this.future == null) {
            this.future = CompletableFuture.completedFuture(new BakedGuiItem(this.guiItem.stack().get().toBukkit(), this.guiItem.eventConsumer()));
        }

        return this.future;
    }
}
