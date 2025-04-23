package com.artillexstudios.axapi.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler<T> {

    default T handle(ResultSet resultSet) throws SQLException {
        return this.handle(resultSet, true);
    }

    T handle(ResultSet resultSet, boolean checkNext) throws SQLException;
}
