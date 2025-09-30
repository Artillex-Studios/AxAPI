package com.artillexstudios.axapi.utils.featureflags.type;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlag;

public final class StringFlag extends FeatureFlag<String> {
    public static final StringFlag TRANSFORMER = new StringFlag("");

    public StringFlag(String def) {
        super(def);
    }

    @Override
    public String transform(String string) {
        return string;
    }
}
