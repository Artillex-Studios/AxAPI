package com.artillexstudios.axapi.libraries.config;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.libraries.Relocation;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class RelocationAdapter implements TypeAdapter<Relocation, Map<String, String>> {

    @Override
    public Relocation deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (!(input instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException();
        }

        return new Relocation((String) map.get("from"), (String) map.get("to"));
    }

    @Override
    public Map<String, String> serialize(TypeAdapterHolder holder, Relocation value, Type type) {
        Map<String, String> serialized = new HashMap<>();
        serialized.put("from", value.from());
        serialized.put("to", value.to());
        return serialized;
    }
}
