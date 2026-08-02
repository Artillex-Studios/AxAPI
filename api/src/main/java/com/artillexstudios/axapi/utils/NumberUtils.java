package com.artillexstudios.axapi.utils;

import org.jspecify.annotations.Nullable;

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

    public static boolean isLong(@Nullable String value) {
        if (value == null) return false;
        try {
            Long.parseLong(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isFloat(@Nullable String value) {
        if (value == null) return false;
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
