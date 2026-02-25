package com.artillexstudios.axapi.database.impl;

import com.artillexstudios.axapi.database.DatabaseConfig;
import com.artillexstudios.axapi.database.DatabaseType;
import com.artillexstudios.axapi.utils.file.FileUtils;
import com.zaxxer.hikari.HikariConfig;

public class H2DatabaseType extends DatabaseType {
    private final String relocated;

    public H2DatabaseType() {
        this("org.h2");
    }

    public H2DatabaseType(String relocated) {
        this.relocated = relocated;
    }

    @Override
    public HikariConfig config(DatabaseConfig databaseConfig) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(this.relocated + ".jdbcx.JdbcDataSource");
        config.addDataSourceProperty("url", "jdbc:h2:./" + FileUtils.getInstance().getFolder() + "/data");
        config.setMaximumPoolSize(databaseConfig.pool.maximumPoolSize);
        config.setMinimumIdle(databaseConfig.pool.minimumIdle);
        return config;
    }

    @Override
    public String name() {
        return "h2";
    }
}
