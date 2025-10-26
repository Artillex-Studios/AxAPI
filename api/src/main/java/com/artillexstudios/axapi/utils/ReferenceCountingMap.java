package com.artillexstudios.axapi.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public final class ReferenceCountingMap<K, V> extends ConcurrentHashMap<K, V> {
    private final Object2IntMap<Object> counter = new Object2IntOpenHashMap<>();

    @Override
    public V put(@NotNull K key, @NotNull V value) {
        synchronized (this.counter) {
            this.counter.compute(key, (k, v) -> v == null ? 1 : v + 1);
        }
        return super.put(key, value);
    }

    @Override
    public V remove(@NotNull Object key) {
        V val;
        synchronized (this.counter) {
            int value = this.counter.getOrDefault(key, -1);
            if (value == -1) {
                return null;
            }

            if (value - 1 == 0) {
                this.counter.removeInt(key);
                val = super.remove(key);
            } else {
                value -= 1;
                val = super.get(key);
            }
            this.counter.put(key, value);
        }

        return val;
    }
}
