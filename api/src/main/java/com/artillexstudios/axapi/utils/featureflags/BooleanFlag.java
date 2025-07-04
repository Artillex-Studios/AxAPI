package com.artillexstudios.axapi.utils.featureflags;

public final class BooleanFlag extends FeatureFlag<Boolean> {
    public static final BooleanFlag TRANSFORMER = new BooleanFlag(true);

    public BooleanFlag(Boolean def) {
        super(def);
    }

    @Override
    public Boolean transform(String string) {
        if (string == null) {
            return null;
        }

        return Boolean.parseBoolean(string);
    }
}
