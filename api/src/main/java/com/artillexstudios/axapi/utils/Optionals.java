package com.artillexstudios.axapi.utils;

import java.util.function.Consumer;

public final class Optionals {

    public static <T> void ifPresent(T object, Consumer<T> consumer) {
        if (object == null) {
            return;
        }

        consumer.accept(object);
    }
}
