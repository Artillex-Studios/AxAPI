package com.artillexstudios.axapi.hologram;

import java.util.concurrent.ConcurrentHashMap;

public class Holograms {
    private static final ConcurrentHashMap<Integer, HologramLine<?>> linesMap = new ConcurrentHashMap<>();

    public static void put(int entityId, HologramLine<?> line) {
        linesMap.put(entityId, line);
    }

    public static void remove(int entityId) {
        linesMap.remove(entityId);
    }

    public static HologramLine<?> byId(int entityId) {
        return linesMap.get(entityId);
    }

    public static ConcurrentHashMap<Integer, HologramLine<?>> getMap() {
        return linesMap;
    }
}
