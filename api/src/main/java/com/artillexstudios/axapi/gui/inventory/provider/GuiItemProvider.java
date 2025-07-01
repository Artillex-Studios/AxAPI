package com.artillexstudios.axapi.gui.inventory.provider;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;

import java.util.concurrent.CompletableFuture;

public interface GuiItemProvider {

    CompletableFuture<BakedGuiItem> provide(HashMapContext context);
}
