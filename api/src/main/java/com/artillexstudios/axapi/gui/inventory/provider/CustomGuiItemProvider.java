package com.artillexstudios.axapi.gui.inventory.provider;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.GuiItem;

import java.util.concurrent.CompletableFuture;

public interface CustomGuiItemProvider<T> extends GuiItemProvider {

    @Override
    default CompletableFuture<GuiItem> provide(HashMapContext context) {
        return this.provide(null, context);
    }

    CompletableFuture<GuiItem> provide(T data, HashMapContext context);
}
