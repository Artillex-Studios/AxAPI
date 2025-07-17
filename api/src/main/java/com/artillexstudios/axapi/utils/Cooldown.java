package com.artillexstudios.axapi.utils;

public interface Cooldown<T> extends Iterable<T> {

    static <Z> Cooldown<Z> create() {
        return new NonSynchronizedCooldown<>();
    }

    static <Z> Cooldown<Z> createSynchronized() {
        return new SynchronizedCooldown<>();
    }

    void addCooldown(T key, long time);

    long getRemaining(T key);

    default boolean hasCooldown(T key) {
        return this.getRemaining(key) > 0;
    }

    int size();

    void remove(T key);

    void clear();
}
