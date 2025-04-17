package com.artillexstudios.axapi.database.impl;

import com.artillexstudios.axapi.database.DatabaseConfig;
import com.artillexstudios.axapi.database.DatabaseType;
import com.zaxxer.hikari.HikariConfig;

public class MySQLDatabaseType implements DatabaseType {
    private final String relocated;

    public MySQLDatabaseType() {
        this("com.mysql");
    }

    public MySQLDatabaseType(String relocated) {
        this.relocated = relocated;
    }

    @Override
    public HikariConfig config(DatabaseConfig databaseConfig) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(this.relocated + ".jdbc.Driver");
        config.setMaximumPoolSize(databaseConfig.pool.maximumPoolSize);
        config.setMinimumIdle(databaseConfig.pool.minimumIdle);
        config.setMaxLifetime(databaseConfig.pool.maximumLifetime);
        config.setKeepaliveTime(databaseConfig.pool.keepaliveTime);
        config.setConnectionTimeout(databaseConfig.pool.connectionTimeout);
        config.addDataSourceProperty("user", databaseConfig.username);
        config.addDataSourceProperty("password", databaseConfig.password);
        config.setJdbcUrl(databaseConfig.url == null ? "jdbc:mysql://" + databaseConfig.address + ":" + databaseConfig.port + "/" + databaseConfig.database : databaseConfig.url);
        return config;
    }

    @Override
    public String name() {
        return "mysql";
    }
}
