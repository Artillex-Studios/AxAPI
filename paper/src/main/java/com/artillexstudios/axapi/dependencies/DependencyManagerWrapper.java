package com.artillexstudios.axapi.dependencies;

import revxrsal.zapper.Dependency;
import revxrsal.zapper.DependencyManager;
import revxrsal.zapper.relocation.Relocation;
import revxrsal.zapper.repository.Repository;

import java.util.regex.Pattern;

public final class DependencyManagerWrapper {
    private static final Pattern COLON = Pattern.compile(":");
    private final DependencyManager dependencyManager;

    public DependencyManagerWrapper(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public void dependency(String dependency, boolean remap) {
        String[] parts = COLON.split(dependency);
        this.dependencyManager.dependency(new Dependency(parts[0].replace("{}", "."), parts[1].replace("{}", "."), parts[2].replace("{}", "."), parts.length == 4 ? parts[3] : null, remap));
    }

    public void dependency(String dependency) {
        this.dependency(dependency, false);
    }

    public void dependency(Dependency dependency) {
        this.dependencyManager.dependency(dependency);
    }

    public void repository(String repository) {
        this.dependencyManager.repository(Repository.maven(repository));
    }

    public void repository(Repository repository) {
        this.dependencyManager.repository(repository);
    }

    public void relocate(String from, String to) {
        this.dependencyManager.relocate(new Relocation(from.replace("{}", "."), to.replace("{}", ".")));
    }

    public void relocate(Relocation relocation) {
        this.dependencyManager.relocate(relocation);
    }

    public DependencyManager wrapped() {
        return this.dependencyManager;
    }
}
