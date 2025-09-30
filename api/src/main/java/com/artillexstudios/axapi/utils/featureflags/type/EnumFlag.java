package com.artillexstudios.axapi.utils.featureflags.type;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlag;

import java.util.Arrays;

public final class EnumFlag<T extends Enum<?>> extends FeatureFlag<T> {
    private final Class<T> clazz;

    public EnumFlag(T def, Class<T> clazz) {
        super(def);
        this.clazz = clazz;
    }

    @Override
    public T transform(String string) {
        if (string == null) {
            return null;
        }

        return Arrays.stream(this.clazz.getEnumConstants())
                .filter(constant -> constant.name().equalsIgnoreCase(string))
                .findFirst()
                .orElseThrow();
    }
}
