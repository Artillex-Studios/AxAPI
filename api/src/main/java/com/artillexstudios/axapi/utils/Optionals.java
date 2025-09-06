package com.artillexstudios.axapi.utils;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@NullMarked
public final class Optionals {

    public static <T> void ifPresent(@Nullable T object, Consumer<T> consumer) {
        if (object == null) {
            return;
        }

        consumer.accept(object);
    }

    public static <T, Z> T applyIfPresent(@Nullable Z value, @Nullable T object, BiFunction<T, Z, T> function) {
        if (value == null) {
            return object;
        }

        return function.apply(object, value);
    }

    public static <T> T applyIf(@Nullable T object, boolean test, UnaryOperator<T> operator) {
        if (test) {
            return operator.apply(object);
        }

        return object;
    }

    @SafeVarargs
    public static <T> T orElse(Supplier<@Nullable T> one, Supplier<@Nullable T>... others) {
        T value = one.get();
        if (value != null) {
            return value;
        }

        for (Supplier<@Nullable T> other : others) {
            value = other.get();
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    @SafeVarargs
    @Nullable
    public static <T> T orElse(@Nullable T one, @Nullable T @Nullable ... others) {
        T value = one;
        if (value != null) {
            return value;
        }

        if (others == null) {
            return null;
        }

        for (T other : others) {
            value = other;
            if (value != null) {
                return value;
            }
        }

        return null;
    }
}
