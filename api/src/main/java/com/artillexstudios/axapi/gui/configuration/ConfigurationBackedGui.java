package com.artillexstudios.axapi.gui.configuration;

import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.gui.inventory.Gui;
import org.bukkit.event.inventory.InventoryType;

import java.util.Locale;

public class ConfigurationBackedGui<T extends Gui> {
    private String title;
    private Integer rows;
    private InventoryType type;

    public ConfigurationBackedGui(YamlConfiguration<?> configuration) {
        this.title = configuration.getString("title");
        this.rows = configuration.getInteger("rows");
        String type = configuration.getString("type");
        this.type = type == null ? InventoryType.CHEST : InventoryType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }

    public T gui() {
        return null;
    }
}
