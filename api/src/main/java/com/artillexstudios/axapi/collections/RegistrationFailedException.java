package com.artillexstudios.axapi.collections;

public class RegistrationFailedException extends Exception {
    private final Object key;
    private final Cause cause;

    public RegistrationFailedException(Object key, Cause cause) {
        super();
        this.key = key;
        this.cause = cause;
    }

    public RegistrationFailedException(String message, Object key, Cause cause) {
        super(message);
        this.key = key;
        this.cause = cause;
    }

    public RegistrationFailedException(String message, Throwable throwable, Object key, Cause cause) {
        super(message, throwable);
        this.key = key;
        this.cause = cause;
    }

    public RegistrationFailedException(Throwable throwable, Object key, Cause cause) {
        super(throwable);
        this.key = key;
        this.cause = cause;
    }

    public Object key() {
        return this.key;
    }

    public Cause cause() {
        return this.cause;
    }

    public enum Cause {
        ALREADY_PRESENT,
        NOT_PRESENT;
    }
}
