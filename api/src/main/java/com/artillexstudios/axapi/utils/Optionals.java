package com.artillexstudios.axapi.utils;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class Optionals {

    public static <T> void ifPresent(T object, Consumer<T> consumer) {
        if (object == null) {
            return;
        }

        consumer.accept(object);
    }

    public static <T, Z> T applyIfPresent(Z value, T object, BiFunction<T, Z, T> function) {
        if (value == null) {
            return object;
        }

        return function.apply(object, value);
    }

    public static <T> T applyIf(T object, boolean test, UnaryOperator<T> operator) {
        if (test) {
            return operator.apply(object);
        }

        return object;
    }
}
