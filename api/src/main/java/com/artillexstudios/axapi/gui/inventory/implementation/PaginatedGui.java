package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.provider.CustomGuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.function.Function;

public class PaginatedGui extends Gui {
    private final Int2ObjectArrayMap<Int2ObjectArrayMap<GuiItemProvider>> pages = new Int2ObjectArrayMap<>();
    private final HashMap<Class<?>, CustomGuiItemProvider<?>> providers;
    private int page = 0;

    public PaginatedGui(Function<HashMapContext, Component> titleProvider, InventoryType type, int rows, HashMap<Class<?>, CustomGuiItemProvider<?>> providers) {
        super(titleProvider, type, rows);
        this.providers = providers;
    }

    public <T> void addItem(T item) {

    }

    public int page() {
        return this.page;
    }

    public void page(int page) {
        this.page = page;
    }

    @Override
    public Int2ObjectArrayMap<GuiItemProvider> providers() {
        return this.pages.get(this.page);
    }
}
