package com.artillexstudios.axapi.database.handler;

import com.artillexstudios.axapi.database.DatabaseHandler;
import com.artillexstudios.axapi.database.ResultHandler;
import com.artillexstudios.axapi.utils.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TransformerHandler<T> implements ResultHandler<T> {
    private final Pair<Function<Object, List<Object>>, Function<List<Object>, Object>> transformer;
    private final DatabaseHandler handler;
    private final Class<T> clazz;

    public TransformerHandler(DatabaseHandler handler, Class<T> clazz) {
        this.handler = handler;
        this.clazz = clazz;
        this.transformer = this.handler.transformer(clazz);
    }

    @Override
    public T handle(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<Object> objects = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            objects.add(resultSet.getObject(i + 1));
        }

        return (T) transformer.second().apply(objects);
    }
}
