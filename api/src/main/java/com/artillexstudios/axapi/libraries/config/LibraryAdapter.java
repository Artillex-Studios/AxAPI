package com.artillexstudios.axapi.libraries.config;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.libraries.Library;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LibraryAdapter implements TypeAdapter<Library, Map<String, Object>> {

    @Override
    public Library deserialize(TypeAdapterHolder holder, Object input, Type type) {
        if (!(input instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException();
        }

        return new Library((String) map.get("group"), (String) map.get("artifact-id"), (String) map.get("version"), (String) map.get("classifier"), UncheckedUtils.unsafeCast(holder.deserialize(map.get("transitive-dependencies"), TypeToken.getParameterized(List.class, Library.class).getType())));
    }

    @Override
    public Map<String, Object> serialize(TypeAdapterHolder holder, Library value, Type type) {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("group", value.group());
        serialized.put("artifact-id", value.artifactId());
        serialized.put("version", value.version());
        serialized.put("classifier", value.classifier());
        serialized.put("transitive-dependencies", holder.serialize(value.transitiveDependencies(), List.class));
        return serialized;
    }
}
