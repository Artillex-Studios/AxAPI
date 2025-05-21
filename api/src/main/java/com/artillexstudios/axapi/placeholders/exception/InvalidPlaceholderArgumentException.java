package com.artillexstudios.axapi.placeholders.exception;

public class InvalidPlaceholderArgumentException extends PlaceholderException {

    public InvalidPlaceholderArgumentException(String placeholder) {
        super("Invalid placeholder argument, %s!".formatted(placeholder));
    }
}
