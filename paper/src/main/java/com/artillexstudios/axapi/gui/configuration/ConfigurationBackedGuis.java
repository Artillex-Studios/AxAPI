package com.artillexstudios.axapi.gui.configuration;

import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationBackedGuis {
    private static final Map<String, ConfigurationBackedGui<?>> guis = new HashMap<>();
    private static final Map<String, ConfigurationBackedGui<?>> guisView = Collections.unmodifiableMap(guis);

    public static <T extends Gui> ConfigurationBackedGui<T> register(ConfigurationBackedGui<T> gui) {
        if (gui.builder().configuration() instanceof YamlConfiguration<?> yaml) {
            register(yaml.path().toString(), gui);
        } else {
            LogUtils.warn("Failed to register gui configuration, because the configuration is not backed by a yaml document, and no id was specified!");
        }

        return gui;
    }

    public static <T extends Gui> ConfigurationBackedGui<T> register(String id, ConfigurationBackedGui<T> gui) {
        guis.put(id, gui);
        return gui;
    }

    public static Map<String, ConfigurationBackedGui<?>> registered() {
        return guisView;
    }
}
