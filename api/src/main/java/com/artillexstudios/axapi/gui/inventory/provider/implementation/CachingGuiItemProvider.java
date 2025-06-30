package com.artillexstudios.axapi.gui.inventory.provider.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.items.WrappedItemStack;

import java.util.concurrent.CompletableFuture;

public class CachingGuiItemProvider implements GuiItemProvider {

    @Override
    public CompletableFuture<GuiItem> provide(WrappedItemStack stack, HashMapContext context) {
        return null;
    }
}
