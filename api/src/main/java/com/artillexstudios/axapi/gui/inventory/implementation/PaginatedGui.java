package com.artillexstudios.axapi.gui.inventory.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.EmptyGuiItemProvider;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.artillexstudios.axapi.utils.mutable.MutableInteger;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class PaginatedGui extends Gui {
    private final HashMap<Class<?>, Function<?, GuiItemProvider>> customProviders;
    private final List<GuiItemProvider> otherProviders = new ArrayList<>();
    private int page = 0;
    private int pageSize = this.size;

    public PaginatedGui(Player player, Function<HashMapContext, Component> titleProvider, InventoryType type, int rows, HashMap<Class<?>, Function<?, GuiItemProvider>> providers) {
        super(player, titleProvider, type, rows);
        this.customProviders = providers;
    }

    public <T> void addItem(T item) {
        Function<Object, GuiItemProvider> guiItemProviderProvider = this.provide(item);
        if (guiItemProviderProvider == null) {
            throw new IllegalStateException("No custom provider for class " + item.getClass() + "!");
        }

        this.otherProviders.add(guiItemProviderProvider.apply(item));
    }

    private Function<Object, GuiItemProvider> provide(Object item) {
        Class<?> clazz = item.getClass();
        Function<?, GuiItemProvider> function = this.customProviders.get(clazz);
        if (function != null) {
            return (Function<Object, GuiItemProvider>) function;
        }

        for (Class<?> cl : ClassUtils.INSTANCE.superClasses(clazz)) {
            function = this.customProviders.get(cl);
            if (function != null) {
                return (Function<Object, GuiItemProvider>) function;
            }
        }

        for (Class<?> cl : ClassUtils.INSTANCE.interfaces(clazz)) {
            function = this.customProviders.get(cl);
            if (function != null) {
                return (Function<Object, GuiItemProvider>) function;
            }
        }

        return null;
    }

    @Override
    public void setItem(int slot, GuiItemProvider provider) {
        super.setItem(slot, provider);
        this.recalculatePageSize();
    }

    @Override
    public void setItem(int slot, GuiItem item, Function<GuiItem, GuiItemProvider> provider) {
        super.setItem(slot, item, provider);
        this.recalculatePageSize();
    }

    @Override
    public void setItem(int slot, GuiItem item) {
        super.setItem(slot, item);
        this.recalculatePageSize();
    }

    private void recalculatePageSize() {
        MutableInteger size = new MutableInteger();
        this.providers.forEach((s, pv) -> {
            if (pv instanceof EmptyGuiItemProvider) {
                size.increment();
            }
        });
        this.pageSize = size.get();
    }

    public int page() {
        return this.page;
    }

    public void page(int page) {
        if (page > this.maxPages() + 1) {
            throw new IllegalStateException("Max pages: " + this.maxPages());
        }

        this.page = page;
    }

    public int maxPages() {
        return Math.ceilDiv(this.otherProviders.size(), this.pageSize);
    }

    @Override
    public Int2ObjectArrayMap<GuiItemProvider> providers() {
        if (this.pageSize == 0 && !this.otherProviders.isEmpty()) {
            LogUtils.warn("This paginated gui has no empty slots for the provided items!");
            LogUtils.warn("Please create some empty slots for them!");
            return super.providers();
        }

        int index = this.page * this.pageSize;
        Int2ObjectArrayMap<GuiItemProvider> pageProviders = super.providers().clone();
        for (Int2ObjectMap.Entry<GuiItemProvider> entry : pageProviders.int2ObjectEntrySet()) {
            if (!(entry.getValue() instanceof EmptyGuiItemProvider)) {
                continue;
            }

            if (index >= this.otherProviders.size()) {
                break;
            }

            entry.setValue(this.otherProviders.get(index));
            index++;
        }

        return pageProviders;
    }
}
