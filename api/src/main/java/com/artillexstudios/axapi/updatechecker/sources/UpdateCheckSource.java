package com.artillexstudios.axapi.updatechecker.sources;

import com.artillexstudios.axapi.updatechecker.ArtifactVersion;
import com.artillexstudios.axapi.updatechecker.UpdateCheck;

public interface UpdateCheckSource {

    UpdateCheck check(ArtifactVersion current);
}
