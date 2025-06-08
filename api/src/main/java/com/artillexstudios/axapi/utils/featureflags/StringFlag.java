package com.artillexstudios.axapi.utils.featureflags;

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
