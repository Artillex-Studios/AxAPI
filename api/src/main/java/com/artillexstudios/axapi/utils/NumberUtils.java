package com.artillexstudios.axapi.utils;

import org.jetbrains.annotations.Nullable;

public class NumberUtils {

    public static boolean isDouble(@Nullable String value) {
        if (value == null) return false;
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isInt(@Nullable String value) {
        if (value == null) return false;
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
