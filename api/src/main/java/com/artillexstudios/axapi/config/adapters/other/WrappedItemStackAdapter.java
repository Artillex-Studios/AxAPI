package com.artillexstudios.axapi.config.adapters.other;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.utils.ItemBuilder;

import java.lang.reflect.Type;
import java.util.Map;

public final class WrappedItemStackAdapter implements TypeAdapter<WrappedItemStack, Map<String, Object>> {

    @Override
    public WrappedItemStack deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (input instanceof Map<?, ?> map) {
            return new ItemBuilder((Map<Object, Object>) map).wrapped();
        }

        throw new IllegalArgumentException();
    }

    @Override
    public Map<String, Object> serialize(TypeAdapterHolder holder, WrappedItemStack value, Type type) {
        return (Map<String, Object>) (Object) new ItemBuilder(value.toBukkit()).serialize(true);
    }
}
