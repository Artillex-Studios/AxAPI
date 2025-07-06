package com.artillexstudios.axapi.gui.inventory.builder;

import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.implementation.StaticGui;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.SimpleGuiItemProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class StaticGuiBuilder extends GuiBuilder<StaticGui, StaticGuiBuilder> {
    private final Int2ObjectArrayMap<GuiItemProvider> providers = new Int2ObjectArrayMap<>();

    // TODO: provide them once
    public void setItem(int slot, GuiItem item) {
        this.setItem(slot, item, SimpleGuiItemProvider::new);
    }

    public void setItem(int slot, GuiItem item, Function<GuiItem, GuiItemProvider> provider) {
        this.providers.put(slot, provider.apply(item));
    }

    public void setItem(int slot, GuiItemProvider provider) {
        this.providers.put(slot, provider);
    }

    @Override
    public StaticGui build(Player player) {
        return new StaticGui(player, this.titleProvider, this.type, this.rows, this.providers);
    }
}
