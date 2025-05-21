package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.InvalidPlaceholderArgumentException;
import com.artillexstudios.axapi.placeholders.exception.PlaceholderException;

public class PlaceholderContext {
    private final Placeholder placeholder;

    public PlaceholderContext(Placeholder placeholder) {
        this.placeholder = placeholder;
    }

    public <T> T argument(String name) throws PlaceholderException {
        for (PlaceholderArgument<?> argument : this.placeholder.arguments().arguments()) {
            if (argument.name().equals(name)) {
                return (T) argument.resolver();
            }
        }

        throw new InvalidPlaceholderArgumentException(name);
    }
}
