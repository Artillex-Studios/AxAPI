package com.artillexstudios.axapi.config.adapters.other;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.database.DatabaseType;
import com.artillexstudios.axapi.database.DatabaseTypes;

import java.lang.reflect.Type;
import java.util.List;

public final class DatabaseTypeAdapter implements TypeAdapter<DatabaseType, String> {

    @Override
    public DatabaseType deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (input instanceof DatabaseType databaseType) {
            return databaseType;
        }

        if (input instanceof String string) {
            return DatabaseTypes.fetch(string);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public String serialize(TypeAdapterHolder holder, DatabaseType value, Type type) {
        return value.name();
    }

    @Override
    public List<String> values(DatabaseType input) {
        return DatabaseTypes.keys();
    }
}
