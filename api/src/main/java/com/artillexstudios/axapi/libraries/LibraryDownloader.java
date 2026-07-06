package com.artillexstudios.axapi.libraries;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LibraryDownloader {
    private final LibraryCache libraryCache;
    private final TransitiveDependencyCollector collector;
    private final List<Library> libraries = new ArrayList<>();
    private final List<Repository> repositories = new ArrayList<>();
    private final List<Relocation> relocations = new ArrayList<>();
    private final List<Path> libraryPaths = new ArrayList<>();
    private final Path librariesFolder;
    private final boolean cleanIfNoCache;

    public LibraryDownloader(Path librariesFolder) {
        this(librariesFolder, false);
    }

    public LibraryDownloader(Path librariesFolder, boolean cleanIfNoCache) {
        this.librariesFolder = librariesFolder;
        this.cleanIfNoCache = cleanIfNoCache;
        this.libraryCache = new LibraryCache(this.librariesFolder);
        this.collector = new TransitiveDependencyCollector(this);
        if (this.cleanIfNoCache && !this.libraryCache.isCacheCreated()) {
            this.deletePath(librariesFolder);
        }
        this.libraryCache.load();
    }

    private void deletePath(Path path) {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            LogUtils.error("An exception occurred while cleaning library cache!", exception);
        }
    }

    public void addLibrary(Library library) {
        if (this.isAlreadyLoaded(library)) {
            return;
        }

        Library cachedLibrary = this.libraryCache.getCachedLibrary(library);
        Path path = this.getPath(library);
        if (cachedLibrary != null && path != null && Files.exists(path)) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Using cached library: {}", library);
            }
            // We already have the correct version of the library cached, we can just load it
            this.libraryPaths.add(path);
            this.libraries.add(cachedLibrary);
            for (Library transitiveDependency : cachedLibrary.transitiveDependencies()) {
                this.addLibrary(transitiveDependency);
            }
            return;
        }

        // remove the old version with transitive dependencies
        this.cleanupFiles(library);
        // find the new library with transitive dependencies
        Library withTransitives = this.collector.withTransitiveDependencies(library);
        this.libraryCache.cache(withTransitives);
        this.libraries.add(withTransitives);
        this.downloadLibrary(withTransitives);
    }

    private boolean isAlreadyLoaded(Library library) {
        for (Library loaded : this.libraries) {
            if (this.libraryCache.checkWithoutVersion(library, loaded)) {
                return true;
            }
        }

        return false;
    }

    private void cleanupFiles(Library library) {
        for (Library transitiveDependency : library.transitiveDependencies()) {
            this.cleanupFiles(transitiveDependency);
        }

        this.deletePath(this.getPath(library));
    }

    private void downloadLibrary(Library library) {
        for (Library transitiveDependency : library.transitiveDependencies()) {
            this.downloadLibrary(transitiveDependency);
        }

        Path path = this.getPath(library);
        // TODO: Relocations
        for (Repository repository : this.getRepositories()) {
            URI jarURI = repository.getJarURI(library);
            try (InputStream inputStream = jarURI.toURL().openStream(); FileOutputStream stream = new FileOutputStream(path.toFile())) {
                byte[] bytes = new byte[8192];
                int length;
                while ((length = inputStream.read(bytes)) != -1) {
                    stream.write(bytes, 0, length);
                }

                this.libraryPaths.add(path);
            } catch (IOException exception) {
                LogUtils.error("An exception occurred while downloading library {}!", library, exception);
            }
        }
    }

    public void addRepository(Repository repository) {
        this.repositories.add(repository);
        if (this.libraryCache.cache(repository)) {
            return;
        }
    }

    public void addRelocation(Relocation relocation) {
        this.relocations.add(relocation);
        if (this.libraryCache.cache(relocation)) {
            return;
        }
        // We need to remove all cached jars and relocate them
        this.deletePath(this.librariesFolder);
        this.libraryCache.save();

        // Download the libraries again
        for (Library library : this.libraries) {
            this.downloadLibrary(library);
        }
    }

    private Path getPath(Library library) {
        if (library == null) {
            return null;
        }

        return this.librariesFolder.resolve("%s.%s-%s%s.jar".formatted(library.group(), library.artifactId(), library.version(), library.classifier() == null || library.classifier().isBlank() ? "" : '-' + library.classifier()));
    }

    public List<Path> getLibraryPaths() {
        return Collections.unmodifiableList(this.libraryPaths);
    }

    public List<Repository> getRepositories() {
        return Collections.unmodifiableList(this.repositories);
    }
}
