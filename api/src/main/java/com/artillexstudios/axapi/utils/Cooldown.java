package com.artillexstudios.axapi.utils;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public class Cooldown<T> {
    private final Object2LongArrayMap<T> cooldowns = new Object2LongArrayMap<>();
    private long minCooldown = Long.MAX_VALUE;
    private long lastCheck = 0;

    public void addCooldown(T key, long time) {
        doHouseKeeping();

        minCooldown = Math.min(minCooldown, time);

        cooldowns.put(key, System.currentTimeMillis() + time);
    }

    public long getRemaining(T key) {
        doHouseKeeping();

        return cooldowns.getOrDefault(key, System.currentTimeMillis()) - System.currentTimeMillis();
    }

    public long getRemainingAsSeconds(T key) {
        doHouseKeeping();

        return (cooldowns.getOrDefault(key, System.currentTimeMillis()) - System.currentTimeMillis()) / 1000;
    }

    public boolean hasCooldown(T key) {
        doHouseKeeping();
        return cooldowns.containsKey(key);
    }

    public void clear() {
        cooldowns.clear();
    }

    public void remove(T key) {
        doHouseKeeping();

        cooldowns.removeLong(key);
    }

    private void doHouseKeeping() {
        if (System.currentTimeMillis() - lastCheck < minCooldown) {
            return;
        }

        ObjectIterator<Object2LongMap.Entry<T>> iterator = cooldowns.object2LongEntrySet().iterator();
        long time = lastCheck = System.currentTimeMillis();

        while (iterator.hasNext()) {
            Object2LongMap.Entry<T> entry = iterator.next();
            if (time >= entry.getLongValue()) {
                iterator.remove();
            }
        }
    }
}
