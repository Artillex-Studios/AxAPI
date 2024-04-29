package com.artillexstudios.axapi.hologram;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class Holograms {
    private static final Int2ObjectLinkedOpenHashMap<HologramLine> linesMap = new Int2ObjectLinkedOpenHashMap<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void put(int entityId, HologramLine line) {
        lock.writeLock().lock();
        try {
            linesMap.put(entityId, line);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void remove(int entityId) {
        lock.writeLock().lock();
        try {
            linesMap.remove(entityId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static HologramLine byId(int entityId) {
        lock.readLock().lock();
        try {
            return linesMap.get(entityId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void getMap(Consumer<Int2ObjectLinkedOpenHashMap<HologramLine>> map) {
        lock.readLock().lock();
        try {
            map.accept(linesMap);
        } finally {
            lock.readLock().unlock();
        }
    }
}
