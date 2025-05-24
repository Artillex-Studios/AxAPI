package com.artillexstudios.axapi.placeholders;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderArgumentResolver<T> {

    @Nullable
    T resolve(String string);
}
