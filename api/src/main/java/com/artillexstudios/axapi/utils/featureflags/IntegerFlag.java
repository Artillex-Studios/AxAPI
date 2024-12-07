package com.artillexstudios.axapi.utils.featureflags;

public final class IntegerFlag extends FeatureFlag<Integer> {
    public static final IntegerFlag TRANSFORMER = new IntegerFlag("", 0);

    public IntegerFlag(String property, Integer def) {
        super(property, def);
    }

    @Override
    public Integer transform(String string) {
        if (string == null) {
            return null;
        }

        return Integer.parseInt(string);
    }
}