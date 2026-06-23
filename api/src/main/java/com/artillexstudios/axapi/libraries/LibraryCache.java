package com.artillexstudios.axapi.libraries;

import com.artillexstudios.axapi.config.YamlConfiguration;

import java.nio.file.Files;
import java.nio.file.Path;

public final class LibraryCache {
    private final YamlConfiguration<?> configuration;
    private boolean isCacheCreated = false;

    public LibraryCache(Path path) {
        Path cacheFilePath = path.resolve("cache.yml");
        this.isCacheCreated = Files.exists(cacheFilePath);
        this.configuration = YamlConfiguration.of(cacheFilePath, CacheConfiguration.class)
                .build();
    }

    public void load() {
        this.configuration.load();
    }

    public boolean isCacheCreated() {
        return this.isCacheCreated;
    }
}
