package com.artillexstudios.axapi.utils;

public class UncheckedUtils {

    public static <T> T unsafeCast(Object object) {
        return (T) object;
    }
}
