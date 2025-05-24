package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.InvalidPlaceholderArgumentException;
import com.artillexstudios.axapi.placeholders.exception.PlaceholderException;
import com.artillexstudios.axapi.placeholders.exception.PlaceholderParameterNotInContextException;

import java.util.regex.Matcher;

public class PlaceholderContext {
    private final Placeholder placeholder;
    private final Matcher matcher;
    private final PlaceholderParameters parameters;

    public PlaceholderContext(Placeholder placeholder, PlaceholderParameters parameters, Matcher matcher) {
        this.placeholder = placeholder;
        this.matcher = matcher;
        this.parameters = parameters == null ? new PlaceholderParameters() : parameters;
    }

    public <T> T resolve(Class<T> clazz) throws PlaceholderParameterNotInContextException {
        return this.parameters.resolve(clazz);
    }

    public <T> T raw(Class<T> clazz) {
        return this.parameters.raw(clazz);
    }

    public <T> boolean contains(Class<T> clazz) {
        return this.parameters.contains(clazz);
    }

    public <T> T argument(String name) throws PlaceholderException {
        for (PlaceholderArgument<?> argument : this.placeholder.arguments().arguments()) {
            if (!argument.name().equals(name)) {
                continue;
            }

            try {
                return (T) argument.resolver().resolve(this.matcher.group(name));
            } catch (IllegalStateException exception) {
                throw new InvalidPlaceholderArgumentException(name);
            }
        }

        throw new InvalidPlaceholderArgumentException(name);
    }
}
