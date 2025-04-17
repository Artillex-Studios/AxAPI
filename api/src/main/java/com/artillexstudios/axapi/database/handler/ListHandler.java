package com.artillexstudios.axapi.database.handler;

import com.artillexstudios.axapi.database.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListHandler<T> implements ResultHandler<List<T>> {
    private final ResultHandler<T> handler;

    public ListHandler(ResultHandler<T> handler) {
        this.handler = handler;
    }

    @Override
    public List<T> handle(ResultSet resultSet) throws SQLException {
        ArrayList<T> objects = new ArrayList<>();
        while (resultSet.next()) {
            objects.add(this.handler.handle(resultSet));
        }

        return objects;
    }
}
