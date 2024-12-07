package com.artillexstudios.axapi.utils.featureflags;

public abstract class FeatureFlag<T> {
    private final T def;
    private T value;

    public FeatureFlag(String property, T def) {
        this.def = def;
        this.value = this.transform(property.isBlank() ? "" :System.getProperty(property));
    }

    public final T get() {
        return this.value == null ? this.def : this.value;
    }

    public abstract T transform(String string);
}
