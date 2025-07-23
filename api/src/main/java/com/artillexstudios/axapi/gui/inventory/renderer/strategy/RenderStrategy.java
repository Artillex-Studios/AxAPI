package com.artillexstudios.axapi.gui.inventory.renderer.strategy;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.Gui;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.Semaphore;

public interface RenderStrategy {

    void render(Gui gui, HashMapContext context, boolean newInventory, Int2ObjectMap<BakedGuiItem> items, Inventory inventory, Semaphore lock);
}
