package com.artillexstudios.axapi.context;

public record ContextKey<T>(String name, Class<T> type) {

    public static <Z> ContextKey<Z> of(String name, Class<Z> type) {
        return new ContextKey<>(name, type);
    }
}
