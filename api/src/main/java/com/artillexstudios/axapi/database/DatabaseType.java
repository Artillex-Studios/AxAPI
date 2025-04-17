package com.artillexstudios.axapi.database;

import com.zaxxer.hikari.HikariConfig;

public interface DatabaseType {

    HikariConfig config(DatabaseConfig config);

    String name();
}
