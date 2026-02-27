package com.artillexstudios.axapi.database.handler;

import com.artillexstudios.axapi.database.ResultHandler;
import com.artillexstudios.axapi.utils.UncheckedUtils;

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
    public T handle(ResultSet resultSet, boolean checkNext) throws SQLException {
        if (checkNext && !resultSet.next()) {
            return null;
        }

        if (this.columnName != null) {
            return UncheckedUtils.unsafeCast(resultSet.getObject(this.columnName));
        }

        return UncheckedUtils.unsafeCast(resultSet.getObject(this.columnId));
    }
}
