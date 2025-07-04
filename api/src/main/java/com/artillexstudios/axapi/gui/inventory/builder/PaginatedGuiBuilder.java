package com.artillexstudios.axapi.gui.inventory.builder;

import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.implementation.PaginatedGui;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Function;

public class PaginatedGuiBuilder extends GuiBuilder<PaginatedGui> {
    private final HashMap<Class<?>, Function<?, GuiItemProvider>> providers = new HashMap<>();

    public <T> PaginatedGuiBuilder withProvider(Class<T> clazz, Function<T, GuiItemProvider> provider) {
        this.providers.put(clazz, provider);
        return this;
    }

    @Override
    public PaginatedGui build(Player player) {
        return new PaginatedGui(player, this.titleProvider, this.type, this.rows, this.providers);
    }
}
