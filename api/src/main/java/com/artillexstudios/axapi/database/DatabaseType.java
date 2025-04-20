package com.artillexstudios.axapi.database;

import com.zaxxer.hikari.HikariConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class DatabaseType {
    private final Map<Class<Object>, Function<Object, List<Object>>> transformers = new HashMap<>();

    public abstract HikariConfig config(DatabaseConfig config);

    public abstract String name();

    public Function<Object, List<Object>> transformers(Class<?> clazz) {
        return this.transformers.get(clazz);
    }

    public <T, Z> void registerTransformer(Class<T> clazz, Function<T, List<Z>> from) {
        this.transformers.put((Class<Object>) clazz, (Function<Object, List<Object>>) (Object) from);
    }
}
