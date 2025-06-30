package com.artillexstudios.axapi.gui.inventory.provider.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

public class EmptyGuiItemProvider implements GuiItemProvider {
    public static final EmptyGuiItemProvider INSTANCE = new EmptyGuiItemProvider();
    private static final WrappedItemStack AIR = WrappedItemStack.wrap(new ItemStack(Material.AIR));
    private static final GuiItem EMPTY_ITEM = new GuiItem(() -> AIR);
    private static final CompletableFuture<GuiItem> FUTURE = CompletableFuture.completedFuture(EMPTY_ITEM);

    @Override
    public CompletableFuture<GuiItem> provide(HashMapContext context) {
        return FUTURE;
    }
}
