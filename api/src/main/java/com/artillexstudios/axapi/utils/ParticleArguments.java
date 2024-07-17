package com.artillexstudios.axapi.utils;

import org.bukkit.Particle;

public class ParticleArguments {
    private final Particle particle;
    private final Object data;

    public ParticleArguments(Particle particle, Object data) {
        this.particle = particle;
        this.data = data;
    }

    public Particle particle() {
        return this.particle;
    }

    public Object data() {
        return this.data;
    }
}

