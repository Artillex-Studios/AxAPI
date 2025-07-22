package com.artillexstudios.axapi.config.adapters;

import com.artillexstudios.axapi.utils.UncheckedUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper class to transform from classes.
 */
public class MapConfigurationGetter implements ConfigurationGetter {
    private final TypeAdapterHolder holder = new TypeAdapterHolder();
    private final Map<Object, Object> wrapped;

    public <T, Z> MapConfigurationGetter(Map<T, Z> section) {
        this.wrapped = UncheckedUtils.unsafeCast(section);
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

    public Set<Object> keys() {
        return new HashSet<>(this.wrapped.keySet());
    }

    public Map<Object, Object> wrapped() {
        return this.wrapped;
    }
}
