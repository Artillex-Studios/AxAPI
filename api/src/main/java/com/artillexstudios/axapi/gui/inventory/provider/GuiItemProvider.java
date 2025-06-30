package com.artillexstudios.axapi.gui.inventory.provider;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.GuiItem;

import java.util.concurrent.CompletableFuture;

public interface GuiItemProvider {

    CompletableFuture<GuiItem> provide(HashMapContext context);
}
