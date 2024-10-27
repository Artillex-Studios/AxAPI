package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;

import java.util.IdentityHashMap;

public final class Context {
    private static final ParameterNotInContextException EXCEPTION = new ParameterNotInContextException();
    private final IdentityHashMap<Class<?>, Object> values;
    private final IdentityHashMap<Class<?>, Pair<Class<?>, ThrowingFunction<Object, Object, ParameterNotInContextException>>> functions;

    private Context(IdentityHashMap<Class<?>, Object> values) {
        this.values = new IdentityHashMap<>(values);
        this.functions = new IdentityHashMap<>(Placeholders.transformers());
    }

    public static Builder builder() {
        return builder(ParseContext.BOTH, ResolutionType.OFFLINE);
    }

    public static Builder builder(ParseContext context) {
        return builder(context, ResolutionType.OFFLINE);
    }

    public static Builder builder(ResolutionType resolutionType) {
        return builder(ParseContext.BOTH, resolutionType);
    }

    public static Builder builder(ParseContext context, ResolutionType resolutionType) {
        return new Builder(context, resolutionType);
    }

    public <T> T raw(Class<T> clazz) {
        return (T) values.get(clazz);
    }

    public <T> boolean has(Class<T> clazz) {
        return values.containsKey(clazz);
    }

    public <T> T resolve(Class<T> clazz) throws ParameterNotInContextException {
        T obj = (T) values.get(clazz);
        if (obj == null) {
            Pair<Class<?>, ThrowingFunction<Object, Object, ParameterNotInContextException>> pair = functions.get(clazz);

            if (pair == null) {
                throw EXCEPTION;
            }

            Object resolved = resolve(pair.first());
            obj = (T) pair.second().apply(resolved);
        }

        return obj;
    }

    public static final class Builder {
        private final IdentityHashMap<Class<?>, Object> values = new IdentityHashMap<>();
        private final ParseContext context;
        private final ResolutionType resolutionType;

        private Builder(ParseContext context, ResolutionType resolutionType) {
            this.context = context;
            this.resolutionType = resolutionType;
        }

        public ParseContext context() {
            return this.context;
        }

        public ResolutionType resolutionType() {
            return this.resolutionType;
        }

        public <T> Builder add(Class<T> clazz, T value) {
            this.values.put(clazz, value);
            return this;
        }

        public Context build() {
            return new Context(this.values);
        }
    }
}
