package com.artillexstudios.axapi.libraries.config;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.libraries.Repository;

import java.lang.reflect.Type;

public final class RepositoryAdapter implements TypeAdapter<Repository, String> {

    @Override
    public Repository deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (!(input instanceof String string)) {
            throw new IllegalArgumentException();
        }

        return new Repository(string);
    }

    @Override
    public String serialize(TypeAdapterHolder holder, Repository value, Type type) {
        return value.url();
    }
}
