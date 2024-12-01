package com.artillexstudios.axapi.utils.featureflags;

public final class LongFlag extends FeatureFlag<Long> {
    public static final LongFlag TRANSFORMER = new LongFlag("", 0L);

    public LongFlag(String property, Long def) {
        super(property, def);
    }

    @Override
    public Long transform(String string) {
        if (string == null) {
            return null;
        }

        return Long.parseLong(string);
    }
}
