package com.artillexstudios.axapi.dependencies;

import com.artillexstudios.axapi.libraries.Library;
import com.artillexstudios.axapi.libraries.LibraryDownloader;
import com.artillexstudios.axapi.libraries.Relocation;
import com.artillexstudios.axapi.libraries.Repository;

import java.util.List;
import java.util.regex.Pattern;

public final class DependencyManagerWrapper {
    private static final Pattern COLON = Pattern.compile(":");
    private final LibraryDownloader libraryDownloader;

    public DependencyManagerWrapper(LibraryDownloader libraryDownloader) {
        this.libraryDownloader = libraryDownloader;
    }

    public void dependency(String dependency) {
        String[] parts = COLON.split(dependency);
        this.libraryDownloader.addLibrary(new Library(parts[0].replace("{}", "."), parts[1].replace("{}", "."), parts[2].replace("{}", "."), parts.length == 4 ? parts[3] : null, List.of()));
    }

    public void dependency(Library dependency) {
        this.libraryDownloader.addLibrary(dependency);
    }

    public void repository(String repository) {
        this.libraryDownloader.addRepository(new Repository(repository));
    }

    public void repository(Repository repository) {
        this.libraryDownloader.addRepository(repository);
    }

    public void relocate(String from, String to) {
        this.libraryDownloader.addRelocation(new Relocation(from.replace("{}", "."), to.replace("{}", ".")));
    }

    public void relocate(Relocation relocation) {
        this.libraryDownloader.addRelocation(relocation);
    }

    public LibraryDownloader wrapped() {
        return this.libraryDownloader;
    }
}
