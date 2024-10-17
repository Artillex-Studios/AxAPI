package com.artillexstudios.axapi.utils;

public record Pair<T, E>(T first, E second) {

    public static <T, E> Pair<T, E> of(T key, E value) {
        return new Pair<>(key, value);
    }

    public static <T, E> Pair<T, E> create(T key, E value) {
        return new Pair<>(key, value);
    }

    public T getKey() {
        return this.first;
    }

    public E getValue() {
        return this.second;
    }
}
