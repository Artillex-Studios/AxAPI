package com.artillexstudios.axapi.database;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.List;
import java.util.Map;

public class DatabaseTypes {
    private static DatabaseType defaultType;
    private static final Object2ObjectArrayMap<String, DatabaseType> registry = new Object2ObjectArrayMap<>();

    public static DatabaseType defaultType() {
        return defaultType;
    }

    public static DatabaseType register(DatabaseType type) {
        return register(type, false);
    }

    public static DatabaseType register(DatabaseType type, boolean def) {
        registry.put(type.name(), type);
        if (def) {
            defaultType = type;
        }

        return type;
    }

    public static DatabaseType fetch(String name) {
        for (Map.Entry<String, DatabaseType> entry : registry.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static List<String> keys() {
        return registry.keySet()
                .stream()
                .toList();
    }
}
