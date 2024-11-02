package com.artillexstudios.axapi.utils.updatechecker;

import java.util.List;

public record UpdateCheck(UpdateCheckResult result, ArtifactVersion version, List<Changelog> changelog, Exception exception) {

    public UpdateCheck(UpdateCheckResult result, ArtifactVersion version, List<Changelog> changelog) {
        this(result, version, changelog, null);
    }
}
