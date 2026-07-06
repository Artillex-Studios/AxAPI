package com.artillexstudios.axapi.libraries;

import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.libraries.config.CacheConfiguration;
import com.artillexstudios.axapi.libraries.config.LibraryAdapter;
import com.artillexstudios.axapi.libraries.config.RelocationAdapter;
import com.artillexstudios.axapi.libraries.config.RepositoryAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class LibraryCache {
    private final YamlConfiguration<?> configuration;
    private boolean isCacheCreated = false;

    public LibraryCache(Path path) {
        Path cacheFilePath = path.resolve("cache.yml");
        this.isCacheCreated = Files.exists(cacheFilePath);
        TypeAdapterHolder.registerExtraAdapter(Library.class, new LibraryAdapter());
        TypeAdapterHolder.registerExtraAdapter(Relocation.class, new RelocationAdapter());
        TypeAdapterHolder.registerExtraAdapter(Repository.class, new RepositoryAdapter());
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

    public Library getCachedLibrary(Library library) {
        for (Library loaded : CacheConfiguration.libraries) {
            Library found = this.checkLibrary(loaded, library);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    // If the library is cached in the regular libraries block, we are fine
    // However, if the cached library is a transitive library, we have a bit of a problem,
    // and we need to check recursively
    public Library checkLibrary(Library loaded, Library other) {
        if (this.checkWithoutVersion(loaded, other)) {
            return loaded;
        }

        for (Library transitiveDependency : loaded.transitiveDependencies()) {
            return this.checkLibrary(transitiveDependency, other);
        }

        return null;
    }

    public boolean checkWithoutVersion(Library library1, Library library2) {
        return library1.group().equals(library2.group()) && Objects.equals(library1.classifier(), library2.classifier()) && library1.artifactId().equals(library2.artifactId());
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
