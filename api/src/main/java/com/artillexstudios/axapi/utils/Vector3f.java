package com.artillexstudios.axapi.utils;

public record Vector3f(float x, float y, float z) {

    public Vector3f() {
        this(0.0f, 0.0f, 0.0f);
    }
}
