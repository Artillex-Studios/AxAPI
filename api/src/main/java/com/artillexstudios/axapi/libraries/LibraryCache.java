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

    public boolean cache(Library library) {
        boolean contains = CacheConfiguration.libraries.contains(library);
        if (!contains) {
            CacheConfiguration.libraries.add(library);
            this.save();
        }
        return contains;
    }

    public boolean cache(Repository repository) {
        boolean contains = CacheConfiguration.repositories.contains(repository);
        if (!contains) {
            CacheConfiguration.repositories.add(repository);
            this.save();
        }
        return contains;
    }

    public boolean cache(Relocation relocation) {
        boolean contains = CacheConfiguration.relocations.contains(relocation);
        if (!contains) {
            CacheConfiguration.relocations.add(relocation);
            this.save();
        }
        return contains;
    }

    public void save() {
        this.configuration.save();
    }

    public boolean isCacheCreated() {
        return this.isCacheCreated;
    }
}
