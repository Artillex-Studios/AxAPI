package com.artillexstudios.axapi.placeholders;

public record Placeholder(String placeholder, PlaceholderArguments arguments) {

    public PlaceholderContext newContext(String placeholder) {
        return new PlaceholderContext(this);
    }
}
