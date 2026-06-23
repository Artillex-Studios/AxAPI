package com.artillexstudios.axapi.libraries;

import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class LibraryDownloader {
    private final LibraryCache libraryCache;
    private final List<Library> libraries = new ArrayList<>();
    private final List<Repository> repositories = new ArrayList<>();
    private final List<Relocation> relocations = new ArrayList<>();
    private final Path librariesFolder;
    private final boolean cleanIfNoCache;

    public LibraryDownloader(Path librariesFolder) {
        this(librariesFolder, false);
    }

    public LibraryDownloader(Path librariesFolder, boolean cleanIfNoCache) {
        this.librariesFolder = librariesFolder;
        this.cleanIfNoCache = cleanIfNoCache;
        this.libraryCache = new LibraryCache(this.librariesFolder);
        if (this.cleanIfNoCache && !this.libraryCache.isCacheCreated()) {
            try {
                Files.delete(librariesFolder);
            } catch (IOException exception) {
                LogUtils.error("An exception occurred while downloading libraries!", exception);
            }
        }
    }

    public void addLibrary(Library library) {
        this.libraries.add(library);
    }

    public void addRepository(Repository repository) {
        this.repositories.add(repository);
    }

    public void addRelocation(Relocation relocation) {
        this.relocations.add(relocation);
    }
}
