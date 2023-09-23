package com.artillexstudios.axapi.utils;

import org.jetbrains.annotations.NotNull;

public class ClassUtils {

    public static boolean classExists(@NotNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
