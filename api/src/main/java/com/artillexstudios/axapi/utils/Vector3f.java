package com.artillexstudios.axapi.utils;

public record Vector3f(float x, float y, float z) {

    public Vector3f() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vector3f(Vector3d location) {
        this((float) location.x(), (float) location.y(), (float) location.z());
    }
}
