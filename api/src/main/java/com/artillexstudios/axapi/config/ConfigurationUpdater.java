package com.artillexstudios.axapi.config;

@FunctionalInterface
public interface ConfigurationUpdater {

    void update(YamlConfiguration config);
}
