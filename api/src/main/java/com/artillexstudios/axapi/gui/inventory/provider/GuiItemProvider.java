package com.artillexstudios.axapi.gui.inventory.provider;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.items.WrappedItemStack;

import java.util.concurrent.CompletableFuture;

public interface GuiItemProvider {

    CompletableFuture<GuiItem> provide(WrappedItemStack stack, HashMapContext context);
}
