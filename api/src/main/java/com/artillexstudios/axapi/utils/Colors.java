package com.artillexstudios.axapi.utils;

public final class Colors {

    public static int color(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static int color(int red, int green, int blue) {
        return color(255, red, green, blue);
    }

    public static int fromVector(Vector3f rgb) {
        return color(transform(rgb.x()), transform(rgb.y()), transform(rgb.z()));
    }

    public static int transform(float value) {
        return floor(value * 255.0F);
    }

    public static int floor(float value) {
        int i = (int) value;
        return value < (float) i ? i - 1 : i;
    }

    public static Vector3f toVector(int rgb) {
        float f = (float) red(rgb) / 255.0F;
        float g = (float) green(rgb) / 255.0F;
        float h = (float) blue(rgb) / 255.0F;
        return new Vector3f(f, g, h);
    }

    public static int red(int argb) {
        return argb >> 16 & 255;
    }

    public static int green(int argb) {
        return argb >> 8 & 255;
    }

    public static int blue(int argb) {
        return argb & 255;
    }
}
