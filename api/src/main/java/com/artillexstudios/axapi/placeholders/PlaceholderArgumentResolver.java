package com.artillexstudios.axapi.placeholders;

import javax.annotation.Nullable;

@FunctionalInterface
public interface PlaceholderArgumentResolver<T> {

    @Nullable
    T resolve(String string);
}
