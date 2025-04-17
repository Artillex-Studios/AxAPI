package com.artillexstudios.axapi.database.handler;

import com.artillexstudios.axapi.database.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleHandler<T> implements ResultHandler<T> {
    private final Integer columnId;
    private final String columnName;

    public SimpleHandler() {
        this(1, null);
    }

    public SimpleHandler(Integer columnId) {
        this(columnId, null);
    }

    public SimpleHandler(String columnName) {
        this(null, columnName);
    }

    public SimpleHandler(Integer columnId, String columnName) {
        this.columnId = columnId;
        this.columnName = columnName;
    }


    @Override
    public T handle(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        if (this.columnName != null) {
            return (T) resultSet.getObject(this.columnName);
        }

        return (T) resultSet.getObject(this.columnId);
    }
}
