package com.artillexstudios.axapi.utils;

import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;

public final class NonSynchronizedCooldown<T> implements Cooldown<T> {
    private final Object2LongMap<T> cooldowns = new Object2LongLinkedOpenHashMap<>();
    private long closestTime = Long.MAX_VALUE;

    @Override
    public void addCooldown(T key, long time) {
        this.doHouseKeeping();

        long newTime = System.currentTimeMillis() + time;
        this.closestTime = Math.min(this.closestTime, newTime);
        this.cooldowns.put(key, newTime);
    }

    @Override
    public long getRemaining(T key) {
        this.doHouseKeeping();

        return this.cooldowns.getOrDefault(key, System.currentTimeMillis()) - System.currentTimeMillis();
    }

    @Override
    public int size() {
        this.doHouseKeeping();

        return this.cooldowns.size();
    }

    @Override
    public void remove(T key) {
        this.doHouseKeeping();

        this.cooldowns.removeLong(key);
    }

    @Override
    public void clear() {
        this.cooldowns.clear();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        this.doHouseKeeping();
        return NonSynchronizedCooldown.this.cooldowns.keySet().iterator();
    }

    private boolean doHouseKeeping() {
        long time = System.currentTimeMillis();
        if (time < this.closestTime) {
            return false;
        }

        boolean didHouseKeeping = false;
        ObjectIterator<Object2LongMap.Entry<T>> iterator = this.cooldowns.object2LongEntrySet().iterator();
        long closestTime = Long.MAX_VALUE;
        while (iterator.hasNext()) {
            Object2LongMap.Entry<T> entry = iterator.next();
            long value = entry.getLongValue();
            if (time >= value) {
                iterator.remove();
                didHouseKeeping = true;
            } else if (value < closestTime) {
                closestTime = value;
            }
        }
        this.closestTime = closestTime;
        return didHouseKeeping;
    }
}
