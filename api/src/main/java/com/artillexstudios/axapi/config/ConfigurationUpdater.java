package com.artillexstudios.axapi.config;

import org.bukkit.configuration.file.YamlConfiguration;

@FunctionalInterface
public interface ConfigurationUpdater {

    void update(YamlConfiguration config);
}
