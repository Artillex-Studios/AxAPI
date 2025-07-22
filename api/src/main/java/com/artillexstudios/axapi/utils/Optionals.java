package com.artillexstudios.axapi.utils;

import java.util.function.BiFunction;
import java.util.function.Consumer;

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
}
