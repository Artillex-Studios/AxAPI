package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.context.ContextKey;
import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.placeholders.exception.PlaceholderParameterNotInContextException;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaceholderParameters extends HashMapContext {
    private static final PlaceholderParameterNotInContextException EXCEPTION = new PlaceholderParameterNotInContextException();

    public PlaceholderParameters(HashMapContext context) {
        super(context);
    }

    public PlaceholderParameters() {
        super();
    }

    public <T> PlaceholderParameters withParameter(Class<T> clazz, T value) {
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Adding parameter class {} with value {}!", clazz, value);
        }
        this.with(ContextKey.of(clazz.getName(), clazz), value);
        return this;
    }

    public <T> PlaceholderParameters withParameter(T value) {
        Set<Class<?>> classes = ClassUtils.INSTANCE.superClasses(value.getClass(), true);
        classes.addAll(List.of(ClassUtils.INSTANCE.interfaces(value.getClass())));
        for (Class<?> clazz : classes) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Adding parameter class {} with value {}!", clazz, value);
            }

            this.with(UncheckedUtils.unsafeCast(ContextKey.of(clazz.getName(), clazz)), value);
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
        return this.getByClass(clazz) != null;
    }

    public <T> T resolve(Class<T> clazz) throws PlaceholderParameterNotInContextException {
        return this.resolve(clazz, new HashSet<>());
    }

    private <T> T resolve(Class<T> clazz, Set<Class<?>> seen) throws PlaceholderParameterNotInContextException {
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Resolving class {}! Current values: {}", clazz, this.values);
        }
        T resolved = this.raw(clazz);

        if (resolved != null) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Found resolved!");
            }
            return resolved;
        }

        if (!seen.add(clazz)) {
            return null;
        }

        for (PlaceholderTransformer<Object, Object> transformer : PlaceholderHandler.transformers()) {
            if (!transformer.to().equals(clazz)) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.debug("Transformer from: {}, transformer to: {}, class: {}", transformer.from(), transformer.to(), clazz);
                }
                continue;
            }

            try {
                Object other = this.resolve(transformer.from());
                return UncheckedUtils.unsafeCast(transformer.function().apply(other));
            } catch (PlaceholderParameterNotInContextException ignored) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.debug("Failed! transformer from: {}, transformer to: {}, class: {}", transformer.from(), transformer.to(), clazz);
                }
                continue;
            }
        }

        throw EXCEPTION;
    }

    @Override
    public PlaceholderParameters merge(HashMapContext other) {
        super.merge(other);
        return this;
    }

    @Override
    public PlaceholderParameters copy() {
        return new PlaceholderParameters(this);
    }

    @Override
    public <T> PlaceholderParameters with(ContextKey<T> key, T value) {
        super.with(key, value);
        return this;
    }

    public <T> T raw(Class<T> clazz) {
        return UncheckedUtils.unsafeCast(this.getByClass(clazz));
    }
}
