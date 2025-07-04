package com.artillexstudios.axapi.database;

import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class RunnableQuery<T> {
    private final Function<Class<?>, Function<Object, List<Object>>> transformer;
    private final Supplier<Connection> connectionSupplier;
    private final ResultHandler<T> resultHandler;
    private final String sql;

    public RunnableQuery(Function<Class<?>, Function<Object, List<Object>>> transformer, Supplier<Connection> connectionSupplier, ResultHandler<T> resultHandler, String sql) {
        this.transformer = transformer;
        this.connectionSupplier = connectionSupplier;
        this.resultHandler = resultHandler;
        this.sql = sql;
    }

    public int update(Object... parameters) {
        try (Connection connection = this.connectionSupplier.get(); PreparedStatement statement = connection.prepareStatement(this.sql)) {
            this.handleStatement(parameters, statement);
            return statement.executeUpdate();
        } catch (SQLException exception) {
            LogUtils.error("An exception occurred while executing sql query {} with parameters: {}!", this.sql, Arrays.toString(parameters));
            throw new RuntimeException(exception);
        }
    }

    public T execute(Object... parameters) {
        try (Connection connection = this.connectionSupplier.get(); PreparedStatement statement = connection.prepareStatement(this.sql, Statement.RETURN_GENERATED_KEYS)) {
            this.handleStatement(parameters, statement);

            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                return this.resultHandler.handle(resultSet);
            }
        } catch (SQLException exception) {
            LogUtils.error("An exception occurred while executing sql query {} with parameters: {}!", this.sql, Arrays.toString(parameters));
            throw new RuntimeException(exception);
        }
    }

    public T query(Object... parameters) {
        try (Connection connection = this.connectionSupplier.get(); PreparedStatement statement = connection.prepareStatement(this.sql)) {
            this.handleStatement(parameters, statement);

            try (ResultSet resultSet = statement.executeQuery()) {
                return this.resultHandler.handle(resultSet);
            }
        } catch (SQLException exception) {
            LogUtils.error("An exception occurred while executing sql query {} with parameters: {}!", this.sql, Arrays.toString(parameters));
            throw new RuntimeException(exception);
        }
    }

    public int[] batch(List<Object[]> batch) {
        try (Connection connection = this.connectionSupplier.get(); PreparedStatement statement = connection.prepareStatement(this.sql)) {
            for (Object[] parameters : batch) {
                this.handleStatement(parameters, statement);

                statement.addBatch();
            }
            return statement.executeBatch();
        } catch (SQLException exception) {
            LogUtils.error("An exception occurred while executing sql query {} with parameters: {}!", this.sql, batch);
            throw new RuntimeException(exception);
        }
    }

    private void handleStatement(Object[] parameters, PreparedStatement statement) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            if (parameter == null) {
                statement.setObject(i + 1, null);
            } else {
                Function<Object, List<Object>> transformer = this.transformer.apply(parameter.getClass());
                if (transformer != null) {
                    List<Object> out = transformer.apply(parameter);
                    int k = 0;
                    int j = i;
                    for (; j < i + out.size(); j++) {
                        statement.setObject(j + 1, out.get(k));
                        k++;
                    }
                    i = j;
                } else {
                    statement.setObject(i + 1, parameter);
                }
            }
        }
    }
}
