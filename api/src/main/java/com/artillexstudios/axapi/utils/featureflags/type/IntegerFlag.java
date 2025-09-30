package com.artillexstudios.axapi.utils.featureflags.type;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlag;

public final class IntegerFlag extends FeatureFlag<Integer> {
    public static final IntegerFlag TRANSFORMER = new IntegerFlag(0);

    public IntegerFlag(Integer def) {
        super(def);
    }

    @Override
    public Integer transform(String string) {
        if (string == null) {
            return null;
        }

        return Integer.parseInt(string);
    }
}
