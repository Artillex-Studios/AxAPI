package com.artillexstudios.axapi.config.adapters;

import com.artillexstudios.axapi.utils.UncheckedUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper class to transform from classes.
 */
public class MapConfigurationGetter implements ConfigurationGetter {
    protected final TypeAdapterHolder holder = new TypeAdapterHolder();
    private final Map<String, Object> wrapped;
    private final Set<String> unmodifiableKeys;

    public <T, Z> MapConfigurationGetter(Map<T, Z> section) {
        this.wrapped = UncheckedUtils.unsafeCast(section);
        this.unmodifiableKeys = Collections.unmodifiableSet(this.wrapped.keySet());
    }

    @Override
    public <T> T get(String path, Class<T> clazz) {
        Object object = this.wrapped.get(path);
        if (object == null) {
            return null;
        }

        return clazz.cast(this.holder.deserialize(object, clazz));
    }

    public void registerAdapter(Class<?> clazz, TypeAdapter<?, ?> adapter) {
        this.holder.registerAdapter(clazz, adapter);
    }

    public void registerAdapters(Map<Class<?>, TypeAdapter<?, ?>> adapters) {
        this.holder.registerAdapters(adapters);
    }

    @Override
    public Set<String> getKeys() {
        return this.unmodifiableKeys;
    }

    public Map<String, Object> wrapped() {
        return this.wrapped;
    }
}
