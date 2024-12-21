package com.artillexstudios.axapi.config;

import java.nio.file.Path;

public final class YamlConfiguration {

    public static YamlConfiguration.Builder of(Path path, Class<?> clazz) {
        return new Builder(path, clazz);
    }

    public static class Builder {
        private final Path path;
        private final Class<?> clazz;

        private Builder(Path path, Class<?> clazz) {
            this.path = path;
            this.clazz = clazz;
        }
    }
}
