package com.artillexstudios.axapi.commands.exception;

public class NotCommandException extends RuntimeException {

    public NotCommandException() {
        super();
    }

    public NotCommandException(String reason) {
        super(reason);
    }
}
