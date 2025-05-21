package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.reflection.ClassUtils;

public class PlaceholderArgument<T> {
    private final String name;
    private final PlaceholderArgumentResolver<T> resolver;

    public PlaceholderArgument(String name, Class<? extends PlaceholderArgumentResolver<T>> resolver) {
        this.name = name;
        this.resolver = ClassUtils.INSTANCE.create(resolver);
    }

    public PlaceholderArgumentResolver<T> resolver() {
        return this.resolver;
    }

    public String name() {
        return this.name;
    }
}
