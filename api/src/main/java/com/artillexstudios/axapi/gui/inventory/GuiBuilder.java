package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.builder.DynamicGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.builder.PaginatedGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.builder.StaticGuiBuilder;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.Function;

public abstract class GuiBuilder<T extends Gui> {
    protected InventoryType type = InventoryType.CHEST;
    protected Function<HashMapContext, Component> titleProvider;
    protected int rows;

    public static DynamicGuiBuilder createDynamic() {
        return new DynamicGuiBuilder();
    }

    public static StaticGuiBuilder createStatic() {
        return new StaticGuiBuilder();
    }

    public static PaginatedGuiBuilder createPaginated() {
        return new PaginatedGuiBuilder();
    }

    public GuiBuilder<T> title(Function<HashMapContext, Component> titleProvider) {
        this.titleProvider = Preconditions.checkNotNull(titleProvider);
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

    public abstract T build(Player player);
}