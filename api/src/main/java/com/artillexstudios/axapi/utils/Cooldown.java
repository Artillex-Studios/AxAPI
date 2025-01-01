package com.artillexstudios.axapi.utils;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.time.Duration;

public class Cooldown<T> {
    private final Object2LongArrayMap<T> cooldowns = new Object2LongArrayMap<>();
    private long closestTime = Long.MAX_VALUE;

    public void addCooldown(T key, long time) {
        this.doHouseKeeping();

        long closest = System.currentTimeMillis() + time;
        this.closestTime = Math.min(this.closestTime, closest);

        this.cooldowns.put(key, closest);
    }

    public void addCooldown(T key, Duration duration) {
        this.doHouseKeeping();

        long closest = System.currentTimeMillis() + duration.toMillis();
        this.closestTime = Math.min(this.closestTime, closest);

        this.cooldowns.put(key, closest);
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

    private void doHouseKeeping() {
        if (System.currentTimeMillis() < this.closestTime) {
            return;
        }

        ObjectIterator<Object2LongMap.Entry<T>> iterator = cooldowns.object2LongEntrySet().iterator();
        long time = System.currentTimeMillis();

        while (iterator.hasNext()) {
            Object2LongMap.Entry<T> entry = iterator.next();
            if (time >= entry.getLongValue()) {
                iterator.remove();
            }
        }
    }
}
