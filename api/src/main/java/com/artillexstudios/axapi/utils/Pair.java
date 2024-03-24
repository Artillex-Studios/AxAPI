package com.artillexstudios.axapi.utils;

import java.util.Objects;

public class Pair<T, E> {
    private final T first;
    private final E second;

    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public T getKey() {
        return first;
    }

    public E getValue() {
        return second;
    }

    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(getFirst(), pair.getFirst()) && Objects.equals(getSecond(), pair.getSecond());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getFirst());
        result = 31 * result + Objects.hashCode(getSecond());
        return result;
    }

    public static <T, E> Pair<T, E> of(T key, E value) {
        return new Pair<>(key, value);
    }

    public static <T, E> Pair<T, E> create(T key, E value) {
        return new Pair<>(key, value);
    }
}
