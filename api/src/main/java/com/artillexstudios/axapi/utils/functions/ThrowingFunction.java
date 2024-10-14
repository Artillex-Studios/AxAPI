package com.artillexstudios.axapi.utils.functions;

public interface ThrowingFunction<T, Z, E extends Exception> {

    Z apply(T value) throws E;
}
