package com.artillexstudios.axapi.placeholders;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderArgumentResolver<T> {

    @Nullable
    T resolve(String string);
}
