package com.artillexstudios.axapi.utils.updatechecker.sources;

import com.artillexstudios.axapi.utils.updatechecker.ArtifactVersion;
import com.artillexstudios.axapi.utils.updatechecker.UpdateCheck;

public interface UpdateCheckSource {

    UpdateCheck check(ArtifactVersion current);
}
