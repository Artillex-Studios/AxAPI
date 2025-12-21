package com.artillexstudios.axapi.utils.functions;

@FunctionalInterface
public interface ThrowingSupplier<T> {

    T get() throws Exception;
}
