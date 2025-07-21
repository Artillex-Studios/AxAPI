package com.artillexstudios.axapi.gui.inventory.provider.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.modifier.WrappedItemStackModifier;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CachingGuiItemProvider implements GuiItemProvider {
    private final GuiItem guiItem;
    private BakedGuiItem bakedGuiItem;
    private CompletableFuture<BakedGuiItem> future;

    public CachingGuiItemProvider(GuiItem guiItem) {
        this.guiItem = guiItem;
    }

    @Override
    public CompletableFuture<BakedGuiItem> provide(HashMapContext context, List<WrappedItemStackModifier> modifiers) {
        if (this.future == null) {
            this.bakedGuiItem = new BakedGuiItem(this.handleModifiers(this.guiItem.stack().apply(context), modifiers).toBukkit(), event -> this.guiItem.eventConsumer().accept(context, event));
            this.future = CompletableFuture.completedFuture(this.bakedGuiItem);
        }

        return this.future;
    }
}
