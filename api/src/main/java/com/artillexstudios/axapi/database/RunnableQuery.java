package com.artillexstudios.axapi.database;

import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.function.Supplier;

public class RunnableQuery<T> {
    private final Supplier<Connection> connectionSupplier;
    private final ResultHandler<T> resultHandler;
    private final String sql;

    public RunnableQuery(Supplier<Connection> connectionSupplier, ResultHandler<T> resultHandler, String sql) {
        this.connectionSupplier = connectionSupplier;
        this.resultHandler = resultHandler;
        this.sql = sql;
    }

    public int update(Object... parameters) {
        try (Connection connection = this.connectionSupplier.get(); PreparedStatement statement = connection.prepareStatement(this.sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            return statement.executeUpdate();
        } catch (SQLException exception) {
            LogUtils.error("An exception occurred while executing sql query {} with parameters: {}!", this.sql, Arrays.toString(parameters));
            throw new RuntimeException(exception);
        }
    }

    public T execute(Object... parameters) {
        try (Connection connection = this.connectionSupplier.get(); PreparedStatement statement = connection.prepareStatement(this.sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            statement.executeUpdate();
            try (ResultSet resultSet = statement.getResultSet()) {
                return this.resultHandler.handle(resultSet);
            }
        } catch (SQLException exception) {
            LogUtils.error("An exception occurred while executing sql query {} with parameters: {}!", this.sql, Arrays.toString(parameters));
            throw new RuntimeException(exception);
        }
    }

    public T query(Object... parameters) {
        try (Connection connection = this.connectionSupplier.get(); PreparedStatement statement = connection.prepareStatement(this.sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return this.resultHandler.handle(resultSet);
            }
        } catch (SQLException exception) {
            LogUtils.error("An exception occurred while executing sql query {} with parameters: {}!", this.sql, Arrays.toString(parameters));
            throw new RuntimeException(exception);
        }
    }
}
