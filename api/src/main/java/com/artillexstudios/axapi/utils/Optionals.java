package com.artillexstudios.axapi.utils;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class Optionals {

    public static <T> void ifPresent(@Nullable T object, @NonNull Consumer<T> consumer) {
        if (object == null) {
            return;
        }

        consumer.accept(object);
    }

    public static <T, Z> T applyIfPresent(@Nullable Z value, @Nullable T object, @NonNull BiFunction<T, Z, T> function) {
        if (value == null) {
            return object;
        }

        return function.apply(object, value);
    }

    public static <T> T applyIf(@Nullable T object, boolean test, @NonNull UnaryOperator<T> operator) {
        if (test) {
            return operator.apply(object);
        }

        return object;
    }

    @SafeVarargs
    public static <T> T orElse(@NonNull Supplier<@Nullable T> one, @NonNull Supplier<@Nullable T>... others) {
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
