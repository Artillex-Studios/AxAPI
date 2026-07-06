package com.artillexstudios.axapi.libraries;

import java.util.List;
import java.util.Objects;

public record Library(String group, String artifactId, String version, String classifier, List<Library> transitiveDependencies) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Library library)) {
            return false;
        }

        return Objects.equals(this.group, library.group) && Objects.equals(this.version, library.version) && Objects.equals(this.artifactId, library.artifactId) && Objects.equals(this.classifier, library.classifier);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.group);
        result = 31 * result + Objects.hashCode(this.artifactId);
        result = 31 * result + Objects.hashCode(this.version);
        result = 31 * result + Objects.hashCode(this.classifier);
        return result;
    }
}
