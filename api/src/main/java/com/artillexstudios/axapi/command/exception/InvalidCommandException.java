package com.artillexstudios.axapi.command.exception;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException() {
        super();
    }

    public InvalidCommandException(String cause) {
        super(cause);
    }
}
