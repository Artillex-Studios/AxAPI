package com.artillexstudios.axapi.utils;

@FunctionalInterface
public interface ThrowingFunction<T, Z, E extends Exception> {

    Z apply(T input) throws E;
}
