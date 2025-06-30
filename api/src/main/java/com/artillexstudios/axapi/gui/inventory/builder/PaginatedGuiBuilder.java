package com.artillexstudios.axapi.gui.inventory.builder;

import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.implementation.PaginatedGui;
import com.artillexstudios.axapi.gui.inventory.provider.CustomGuiItemProvider;

import java.util.HashMap;

public class PaginatedGuiBuilder extends GuiBuilder<PaginatedGui> {
    private final HashMap<Class<?>, CustomGuiItemProvider<?>> providers = new HashMap<>();

    public <T> PaginatedGuiBuilder withProvider(Class<T> clazz, CustomGuiItemProvider<T> provider) {
        this.providers.put(clazz, provider);
        return this;
    }

    @Override
    public PaginatedGui build() {
        return null;
    }
}
