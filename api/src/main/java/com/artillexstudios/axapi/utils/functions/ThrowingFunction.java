package com.artillexstudios.axapi.utils.functions;

@FunctionalInterface
public interface ThrowingFunction<T, Z, E extends Exception> {

    Z apply(T value) throws E;
}
