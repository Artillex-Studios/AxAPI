package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.gui.inventory.builder.DynamicGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.builder.PaginatedGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.builder.StaticGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.provider.ItemStackProvider;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;

public abstract class GuiBuilder<T extends Gui> {
    private final HashMap<Class<?>, ItemStackProvider<?>> providers = new HashMap<>();
    private InventoryType type;
    private Component title;
    private int rows;

    public static DynamicGuiBuilder createDynamic() {
        return new DynamicGuiBuilder();
    }

    public static StaticGuiBuilder createStatic() {
        return new StaticGuiBuilder();
    }

    public static PaginatedGuiBuilder createPaginated() {
        return new PaginatedGuiBuilder();
    }

    public GuiBuilder<T> title(Component title) {
        this.title = Preconditions.checkNotNull(title);
        return this;
    }

    public GuiBuilder<T> rows(int rows) {
        this.rows = rows;
        return this;
    }

    public GuiBuilder<T> inventoryType(InventoryType type) {
        this.type = type;
        return this;
    }

    public <Z> GuiBuilder<T> withProvider(Class<Z> clazz, ItemStackProvider<Z> provider) {
        this.providers.put(clazz, provider);
        return this;
    }

    public abstract T build();
}