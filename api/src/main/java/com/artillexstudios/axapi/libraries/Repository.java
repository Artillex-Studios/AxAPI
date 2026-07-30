package com.artillexstudios.axapi.libraries;

import java.net.URI;

public record Repository(String url) {

    public URI getJarURI(Library library) {
        return URI.create(this.getPath(library) + ".jar");
    }

    public URI getPomURI(Library library) {
        return URI.create(this.getPath(library) + ".pom");
    }

    public URI getMd5ChecksumURI(Library library) {
        return URI.create(this.getPath(library) + ".jar.md5");
    }

    private String getPath(Library library) {
        return this.url + String.format("%s/%s/%s/%s-%s%s", library.group().replace(".", "/"), library.artifactId(), library.version(), library.artifactId(), library.version(), library.classifier() == null || library.classifier().isBlank() ? "" : '-' + library.classifier());
    }
}
