package com.artillexstudios.axapi.config;

import it.unimi.dsi.fastutil.ints.IntIntPair;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class YamlConfiguration {

    public static YamlConfiguration.Builder of(Path path, Class<?> clazz) {
        return new Builder(path, clazz);
    }

    public void load() {
        this.runUpdaters();

    }

    private void runUpdaters() {

    }

    public static class Builder {
        private final Path path;
        private final Class<?> clazz;
        private final Map<IntIntPair, ConfigurationUpdater> updaters = new HashMap<>();

        private Builder(Path path, Class<?> clazz) {
            this.path = path;
            this.clazz = clazz;
        }

        public Builder addUpdater(int fromVersion, int toVersion, ConfigurationUpdater updater) {
            this.updaters.put(IntIntPair.of(fromVersion, toVersion), updater);
            return this;
        }

        public YamlConfiguration build() {
            return null;
        }
    }
}
