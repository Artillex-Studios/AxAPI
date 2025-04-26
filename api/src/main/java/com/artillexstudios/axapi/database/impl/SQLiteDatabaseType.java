package com.artillexstudios.axapi.database.impl;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.database.DatabaseConfig;
import com.artillexstudios.axapi.database.DatabaseType;
import com.zaxxer.hikari.HikariConfig;

import java.util.List;
import java.util.UUID;

public class SQLiteDatabaseType extends DatabaseType {
    private final String relocated;

    public SQLiteDatabaseType() {
        this("org.sqlite");
    }

    public SQLiteDatabaseType(String relocated) {
        this.relocated = relocated;
        this.registerTransformer(UUID.class, uuid -> List.of(uuid.toString()));
    }

    @Override
    public HikariConfig config(DatabaseConfig databaseConfig) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(this.relocated + ".JDBC");
        config.setJdbcUrl(databaseConfig.url == null ? "jdbc:sqlite:" + AxPlugin.getPlugin(AxPlugin.class).getDataFolder() + "/data" : databaseConfig.url);
        config.setMaximumPoolSize(databaseConfig.pool.maximumPoolSize);
        config.setMinimumIdle(databaseConfig.pool.minimumIdle);
        return config;
    }

    @Override
    public String name() {
        return "sqlite";
    }
}
