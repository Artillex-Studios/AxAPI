package com.artillexstudios.axapi.database;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.database.handler.SimpleHandler;
import com.artillexstudios.axapi.database.impl.MySQLDatabaseType;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class DatabaseHandler {
    private final AxPlugin plugin;
    private final DatabaseConfig config;
    private final Supplier<Connection> connectionSupplier;
    private HikariDataSource dataSource;

    public DatabaseHandler(AxPlugin plugin, DatabaseConfig config, Supplier<Connection> connectionSupplier) {
        this.plugin = plugin;
        this.config = config;
        this.connectionSupplier = connectionSupplier;
    }

    public DatabaseHandler(AxPlugin plugin, DatabaseConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.connectionSupplier = this.createHikariConfig();
    }

    private Supplier<Connection> createHikariConfig() {
        HikariConfig config = this.config.type.config(this.config);
        config.setPoolName("axapi-" + this.plugin.getName() + "-database-pool");
        this.editConfig(config);
        if (this.config.url != null && !(this.config.type instanceof MySQLDatabaseType)) {
            config.addDataSourceProperty("url", this.config.url);
        }
        this.dataSource = new HikariDataSource(config);

        return () -> {
            try {
                return this.dataSource.getConnection();
            } catch (SQLException exception) {
                LogUtils.error("Failed to acquire connection from datasource!", exception);
                throw new RuntimeException(exception);
            }
        };
    }

    public void editConfig(HikariConfig config) {

    }

    public <T, Z> void addTransformer(Class<T> clazz, Function<T, List<Z>> from) {
        this.config.type.registerTransformer(clazz, from);
    }

    public Function<Object, List<Object>> transformer(Class<?> clazz) {
        return this.config.type.transformers(clazz);
    }

    public <T> DatabaseQuery<T> rawQuery(String sql) {
        return this.rawQuery(sql, new SimpleHandler<>());
    }

    public <T> DatabaseQuery<T> rawQuery(String sql, ResultHandler<T> resultHandler) {
        return new DatabaseQuery<>(this, resultHandler, this.processSQL(sql));
    }

    public <T> DatabaseQuery<T> query(String name) {
        return this.query(name, new SimpleHandler<>());
    }

    public <T> DatabaseQuery<T> query(String name, ResultHandler<T> resultHandler) {
        return new DatabaseQuery<>(this, resultHandler, this.processSQL(this.fetchQuery(name)));
    }

    private String fetchQuery(String name) {
        try {
            return this.fetchQuery0(name);
        } catch (IOException exception) {
            LogUtils.error("An exception occurred while fetching query {}!", name, exception);
            throw new RuntimeException(exception);
        }
    }

    private String processSQL(String sql) {
        return sql.replace("$table_prefix", this.config.tablePrefix());
    }

    private String fetchQuery0(String name) throws IOException {
        InputStream resource = null;
        Path path = this.plugin.getDataFolder().toPath().resolve("sql/" + this.config.type.name() + "/" + name + ".sql");
        if (Files.exists(path)) {
            resource = Files.newInputStream(path);
        }

        path = this.plugin.getDataFolder().toPath().resolve("sql/base/" + name + ".sql");
        if (Files.exists(path)) {
            resource = Files.newInputStream(path);
        }

        if (resource == null) {
            resource = this.plugin.getResource("sql/" + this.config.type.name() + "/" + name + ".sql");
        }

        if (resource == null) {
            resource = this.plugin.getResource("sql/base/" + name + ".sql");
        }

        if (resource == null) {
            LogUtils.warn("Failed to find sql query with name {}!", name);
            return "";
        }

        try {
            return new String(resource.readAllBytes(), StandardCharsets.UTF_8);
        } finally {
            resource.close();
        }
    }

    public Connection connection() {
        return this.connectionSupplier.get();
    }

    public void close() {
        if (this.dataSource == null) {
            return;
        }

        this.dataSource.close();
    }
}
