package com.artillexstudios.axapi.placeholders;

import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface PlaceholderArgumentResolver<T> {

    @Nullable
    T resolve(String string);
}
