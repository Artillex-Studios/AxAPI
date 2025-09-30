package com.artillexstudios.axapi.utils;

public record Vector3d(double x, double y, double z) {

    public Vector3d() {
        this(0.0D, 0.0D, 0.0D);
    }
}
