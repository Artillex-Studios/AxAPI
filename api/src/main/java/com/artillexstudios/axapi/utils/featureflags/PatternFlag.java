package com.artillexstudios.axapi.utils.featureflags;

import java.util.regex.Pattern;

public final class PatternFlag extends FeatureFlag<Pattern> {
    public static final PatternFlag TRANSFORMER = new PatternFlag(Pattern.compile(" "));

    public PatternFlag(Pattern def) {
        super(def);
    }

    @Override
    public Pattern transform(String string) {
        if (string == null) {
            return null;
        }

        return Pattern.compile(string);
    }
}
