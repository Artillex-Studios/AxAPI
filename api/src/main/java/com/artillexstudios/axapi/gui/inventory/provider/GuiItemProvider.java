package com.artillexstudios.axapi.gui.inventory.provider;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.AsyncGuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.CachingGuiItemProvider;

import java.util.concurrent.CompletableFuture;

public interface GuiItemProvider {

    static GuiItemProvider async(GuiItem item) {
        return new AsyncGuiItemProvider(item);
    }

    static GuiItemProvider caching(GuiItem item) {
        return new CachingGuiItemProvider(item);
    }

    CompletableFuture<BakedGuiItem> provide(HashMapContext context);
}
