package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.Function;

public class StaticGui extends Gui {

    public StaticGui(Player player, Function<HashMapContext, Component> titleProvider, InventoryType type, int rows, Int2ObjectArrayMap<GuiItemProvider> providers) {
        super(player, titleProvider, type, rows);
        this.providers.clear();
        this.providers.putAll(providers);
    }

    @Override
    public void setItem(int slot, GuiItem item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setItem(int slot, GuiItemProvider provider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setItem(int slot, GuiItem item, Function<GuiItem, GuiItemProvider> provider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateTitle() {
        // NO-OP
    }

    @Override
    public Int2ObjectArrayMap<GuiItemProvider> providers() {
        return super.providers();
    }
}
