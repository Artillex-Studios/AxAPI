package com.artillexstudios.axapi.utils;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public final class Cooldown<T> {
    private final Object2LongMap<T> cooldowns;
    private final boolean synchronize;
    private final AtomicLong closestTime = new AtomicLong(Long.MAX_VALUE);

    public Cooldown() {
        this(false);
    }

    public Cooldown(boolean synchronize) {
        this.synchronize = synchronize;
        if (synchronize) {
            this.cooldowns = Object2LongMaps.synchronize(new Object2LongArrayMap<>());
        } else {
            this.cooldowns = new Object2LongArrayMap<>();
        }
    }

    public void addCooldown(T key, long time) {
        this.doHouseKeeping();

        long newTime = System.currentTimeMillis() + time;
        this.closestTime.getAndUpdate(a -> Math.min(a, newTime));
        this.cooldowns.put(key, newTime);
    }

    public long getRemaining(T key) {
        this.doHouseKeeping();

        return this.cooldowns.getOrDefault(key, System.currentTimeMillis()) - System.currentTimeMillis();
    }

    public long getRemainingAsSeconds(T key) {
        this.doHouseKeeping();

        return (this.cooldowns.getOrDefault(key, System.currentTimeMillis()) - System.currentTimeMillis()) / 1000;
    }

    public boolean hasCooldown(T key) {
        this.doHouseKeeping();
        return this.cooldowns.containsKey(key);
    }

    public void clear() {
        this.cooldowns.clear();
    }

    public void remove(T key) {
        this.doHouseKeeping();

        this.cooldowns.removeLong(key);
    }

    public int size() {
        this.doHouseKeeping();

        return this.cooldowns.size();
    }

    public void forEach(Consumer<ObjectIterator<Object2LongMap.Entry<T>>> iteratorConsumer) {
        this.doHouseKeeping();
        if (this.synchronize) {
            synchronized (this.cooldowns) {
                iteratorConsumer.accept(this.cooldowns.object2LongEntrySet().iterator());
            }
        } else {
            iteratorConsumer.accept(this.cooldowns.object2LongEntrySet().iterator());
        }
    }

    private void doHouseKeeping() {
        if (System.currentTimeMillis() < this.closestTime.get()) {
            return;
        }

        if (this.synchronize) {
            synchronized (this.cooldowns) {
                ObjectIterator<Object2LongMap.Entry<T>> iterator = this.cooldowns.object2LongEntrySet().iterator();
                long time = System.currentTimeMillis();

                while (iterator.hasNext()) {
                    Object2LongMap.Entry<T> entry = iterator.next();
                    if (time >= entry.getLongValue()) {
                        iterator.remove();
                    }
                }
            }
        } else {
            ObjectIterator<Object2LongMap.Entry<T>> iterator = this.cooldowns.object2LongEntrySet().iterator();
            long time = System.currentTimeMillis();

            while (iterator.hasNext()) {
                Object2LongMap.Entry<T> entry = iterator.next();
                if (time >= entry.getLongValue()) {
                    iterator.remove();
                }
            }
        }
    }
}
