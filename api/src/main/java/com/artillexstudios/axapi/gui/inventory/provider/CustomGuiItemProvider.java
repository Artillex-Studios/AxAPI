package com.artillexstudios.axapi.gui.inventory.provider;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;

import java.util.concurrent.CompletableFuture;

public interface CustomGuiItemProvider<T> extends GuiItemProvider {

    @Override
    default CompletableFuture<BakedGuiItem> provide(HashMapContext context) {
        return this.provide(null, context);
    }

    CompletableFuture<BakedGuiItem> provide(T data, HashMapContext context);
}
