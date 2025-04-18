package com.artillexstudios.axapi.database;

import com.artillexstudios.axapi.utils.Pair;
import com.zaxxer.hikari.HikariConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class DatabaseType {
    private final Map<Class<Object>, Pair<Function<Object, List<Object>>, Function<List<Object>, Object>>> transformers = new HashMap<>();

    public abstract HikariConfig config(DatabaseConfig config);

    public abstract String name();

    public Pair<Function<Object, List<Object>>, Function<List<Object>, Object>> transformers(Class<?> clazz) {
        return this.transformers.get(clazz);
    }

    public <T, Z> void registerTransformer(Class<T> clazz, Function<T, List<Z>> from, Function<List<Z>, T> to) {
        this.transformers.put((Class<Object>) clazz, (Pair<Function<Object, List<Object>>, Function<List<Object>, Object>>) (Object) Pair.of(from, to));
    }
}
