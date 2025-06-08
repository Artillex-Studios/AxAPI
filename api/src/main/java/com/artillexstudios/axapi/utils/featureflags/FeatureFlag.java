package com.artillexstudios.axapi.utils.featureflags;

import com.artillexstudios.axapi.utils.logging.LogUtils;

public abstract class FeatureFlag<T> {
    private boolean refreshed = false;
    private final T def;
    private T value;

    public FeatureFlag(T def) {
        this.def = def;
    }

    public void refresh(String property) {
        this.refreshed = true;
        this.value = property.isBlank() ? null : this.transform(System.getProperty(property));
    }

    public void set(T value) {
        if (!this.refreshed) {
            LogUtils.warn("This FeatureFlag has not been refreshed! Please, refresh it before trying to use it!");
        }

        this.value = value;
    }

    public final T get() {
        if (!this.refreshed) {
            LogUtils.warn("This FeatureFlag has not been refreshed! Please, refresh it before trying to use it!");
            return this.def;
        }

        return this.value == null ? this.def : this.value;
    }

    public abstract T transform(String string);
}
