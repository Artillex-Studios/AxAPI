package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.CachingGuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.EmptyGuiItemProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.Function;

public abstract class Gui {
    private final Int2ObjectArrayMap<GuiItemProvider> providers = new Int2ObjectArrayMap<>();
    private final InventoryType type;
    private Component title;
    private int rows;

    public Gui(Component title, InventoryType type, int rows) {
        this.title = title;
        this.type = type;
        this.rows = rows;

        int size = rows * 9;
        if (this.type != InventoryType.CHEST) {
            size = this.type.getDefaultSize();
        }

        for (int i = 0; i < size; i++) {
            this.providers.put(i, EmptyGuiItemProvider.INSTANCE);
        }
    }

    public void setItem(int slot, GuiItem item) {
        this.setItem(slot, item, CachingGuiItemProvider::new);
    }

    public void setItem(int slot, GuiItem item, Function<GuiItem, GuiItemProvider> provider) {
        this.providers.put(slot, provider.apply(item));
    }

    public void setItem(int slot, GuiItemProvider provider) {
        this.providers.put(slot, provider);
    }

    public int rows() {
        return this.rows;
    }

    public Component title() {
        return this.title;
    }

    public void title(Component title) {
        this.title = title;
    }

    public InventoryType type() {
        return this.type;
    }

    public void open(Player player) {

    }
}
