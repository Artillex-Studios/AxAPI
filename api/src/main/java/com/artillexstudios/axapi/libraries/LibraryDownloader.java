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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LibraryDownloader {
    private final LibraryCache libraryCache;
    private final TransitiveDependencyCollector collector;
    private final RelocationHelper helper;
    private final List<Library> libraries = new ArrayList<>();
    private final List<Repository> repositories = new ArrayList<>();
    private final List<Relocation> relocations = new ArrayList<>();
    private final List<Path> libraryPaths = new ArrayList<>();
    private final Path librariesFolder;
    private final boolean cleanIfNoCache;
    private boolean loaded = false;

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
        this.addRepository(new Repository("https://repo.artillex-studios.com/releases/"));
        this.addRepository(new Repository("https://repo1.maven.org/maven2/"));
        this.helper = new RelocationHelper(this);
        this.loaded = true;
    }

    private void deletePath(Path path) {
        if (!Files.exists(path)) {
            return;
        }

        LogUtils.info("Cleaning up library at path: {}", path);
        try {
            if (Files.isDirectory(path)) {
                Files.list(path).forEach(this::deletePath);
            }
            Files.delete(path);
        } catch (IOException exception) {
            LogUtils.error("An exception occurred while cleaning library cache!", exception);
        }
    }

    public Path addLibrary(Library library) {
        return this.addLibrary(library, false);
    }

    public Path addLibrary(Library library, boolean transitive) {
        long start = System.currentTimeMillis();
        LogUtils.info("Loading library {}", library);
        if (this.isAlreadyLoaded(library)) {
            LogUtils.info("Library {} has already been loaded, skipping!", library);
            return null;
        }

        Library cachedLibrary = this.libraryCache.getCachedLibrary(library);
        Path path = this.getPath(library);
        if (cachedLibrary != null && path != null && Files.exists(path)) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Using cached library: {}", library);
            }
            LogUtils.info("Loading {} from cache...", library);
            // We already have the correct version of the library cached, we can just load it
            this.libraryPaths.add(path);
            this.libraries.add(cachedLibrary);
            for (Library transitiveDependency : cachedLibrary.transitiveDependencies()) {
                this.addLibrary(transitiveDependency, true);
            }
            LogUtils.info("Library {} loaded in {}ms from cache!", library, System.currentTimeMillis() - start);
            return path;
        }

        if (path != null && Files.exists(path) && !this.loaded) {
            return path;
        }

        // remove the old version with transitive dependencies
        if (cachedLibrary != null) {
            LogUtils.info("Library {} has an older version, cleaning up...", library);
            this.cleanupFiles(cachedLibrary);
        }
        // find the new library with transitive dependencies
        Library withTransitives = library;
        if (this.loaded) {
            LogUtils.info("Searching for transitive dependencies of library: {}...", library);
            withTransitives = this.collector.withTransitiveDependencies(library);
            LogUtils.info("Found {} transitive dependencies of library: {}!", withTransitives.transitiveDependencies().size(), withTransitives);
            if (!transitive) {
                this.libraryCache.cache(withTransitives);
            }
        }

        if (this.loaded) {
            this.libraries.add(withTransitives);
        }
        this.downloadLibrary(withTransitives);
        LogUtils.info("Downloaded library {} in {}ms!", library, System.currentTimeMillis() - start);
        return path;
    }

    private boolean isAlreadyLoaded(Library library) {
        for (Library loaded : this.libraries) {
            if (LibraryCache.checkWithoutVersion(library, loaded)) {
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

        LogUtils.info("Downloading library {}...", library);
        Path path = this.getPath(library);
        for (Repository repository : this.getRepositories()) {
            URI jarURI = repository.getJarURI(library);
            Path resolve = this.loaded ? path.resolveSibling(path.getFileName() + ".resolving") : path;
            try (InputStream inputStream = jarURI.toURL().openStream(); FileOutputStream stream = new FileOutputStream(resolve.toFile())) {
                byte[] bytes = new byte[8192];
                int length;
                while ((length = inputStream.read(bytes)) != -1) {
                    stream.write(bytes, 0, length);
                }

                if (this.loaded) {
                    LogUtils.info("Relocating library: {}", library);
                    this.helper.relocate(resolve, path, this.createRelocationsMap());
                    this.deletePath(resolve);
                    LogUtils.info("Relocated library: {}", library);
                }
                this.libraryPaths.add(path);
            } catch (IOException exception) {
                exception.printStackTrace();
                LogUtils.error("An exception occurred while downloading library {}!", library, exception);
            }
            break;
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

        LogUtils.info("Re-downloading libraries, as a new relocation has been added!");
        // We need to remove all cached jars and relocate them
        this.deletePath(this.librariesFolder);
        this.libraryCache.save();
        this.collector.reset();

        // Download the libraries again
        for (Library library : this.libraries) {
            this.downloadLibrary(library);
        }
    }

    private Map<String, String> createRelocationsMap() {
        Map<String, String> relocations = new HashMap<>();
        for (Relocation relocation : this.relocations) {
            relocations.put(relocation.from(), relocation.to());
        }

        return relocations;
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
