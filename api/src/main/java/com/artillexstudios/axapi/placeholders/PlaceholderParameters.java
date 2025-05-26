package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.placeholders.exception.PlaceholderParameterNotInContextException;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.IdentityHashMap;
import java.util.Map;

public class PlaceholderParameters {
    private static final FeatureFlags flags = AxPlugin.getPlugin(AxPlugin.class).flags();
    private static final PlaceholderParameterNotInContextException EXCEPTION = new PlaceholderParameterNotInContextException();
    private final Map<Class<?>, Object> values = new IdentityHashMap<>();

    public <T> PlaceholderParameters withParameter(Class<T> clazz, T value) {
        if (flags.DEBUG.get()) {
            LogUtils.debug("Adding parameter class {} with value {}!", clazz, value);
        }
        this.values.put(clazz, value);
        return this;
    }

    public <T> PlaceholderParameters withParameter(T value) {
        for (Class<?> clazz : ClassUtils.INSTANCE.superClasses(value.getClass(), true)) {
            if (flags.DEBUG.get()) {
                LogUtils.debug("Adding parameter class {} with value {}!", clazz, value);
            }

            this.values.put(clazz, value);
        }
        return this;
    }

    public PlaceholderParameters withParameters(Object... values) {
        for (Object value : values) {
            this.withParameter(value);
        }
        return this;
    }

    public <T> boolean contains(Class<T> clazz) {
        return this.values.containsKey(clazz);
    }

    public <T> T resolve(Class<T> clazz) throws PlaceholderParameterNotInContextException {
        if (flags.DEBUG.get()) {
            LogUtils.debug("Resolving class {}! Current values: {}", clazz, this.values);
        }
        T resolved = this.raw(clazz);

        if (resolved != null) {
            if (flags.DEBUG.get()) {
                LogUtils.debug("Found resolved!");
            }
            return resolved;
        }

        for (PlaceholderTransformer<Object, Object> transformer : PlaceholderHandler.transformers()) {
            if (!transformer.to().equals(clazz)) {
                if (flags.DEBUG.get()) {
                    LogUtils.debug("Transformer from: {}, transformer to: {}, class: {}", transformer.from(), transformer.to(), clazz);
                }
                continue;
            }

            try {
                Object other = this.resolve(transformer.from());
                return (T) transformer.function().apply(other);
            } catch (PlaceholderParameterNotInContextException ignored) {
                if (flags.DEBUG.get()) {
                    LogUtils.debug("Failed! transformer from: {}, transformer to: {}, class: {}", transformer.from(), transformer.to(), clazz);
                }
                continue;
            }
        }

        throw EXCEPTION;
    }

    public <T> T raw(Class<T> clazz) {
        return (T) this.values.get(clazz);
    }
}
