package com.artillexstudios.axapi.config;

import com.artillexstudios.axapi.config.annotation.ConfigurationPart;

@FunctionalInterface
public interface ConfigurationUpdater {

    <T extends ConfigurationPart> void update(YamlConfiguration<T> config);
}
