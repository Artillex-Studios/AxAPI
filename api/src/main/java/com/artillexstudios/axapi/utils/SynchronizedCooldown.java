package com.artillexstudios.axapi.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class SynchronizedCooldown<T> implements Cooldown<T> {
    private final NonSynchronizedCooldown<T> delegate = new NonSynchronizedCooldown<>();

    @Override
    public void addCooldown(T key, long time) {
        synchronized (this.delegate) {
            this.delegate.addCooldown(key, time);
        }
    }

    @Override
    public long getRemaining(T key) {
        synchronized (this.delegate) {
            return this.delegate.getRemaining(key);
        }
    }

    @Override
    public int size() {
        synchronized (this.delegate) {
            return this.delegate.size();
        }
    }

    @Override
    public void remove(T key) {
        synchronized (this.delegate) {
            this.delegate.remove(key);
        }
    }

    @Override
    public void clear() {
        synchronized (this.delegate) {
            this.delegate.clear();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        synchronized (this.delegate) {
            return this.delegate.iterator();
        }
    }
}
