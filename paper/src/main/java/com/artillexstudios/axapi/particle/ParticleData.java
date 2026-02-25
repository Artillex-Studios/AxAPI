package com.artillexstudios.axapi.particle;

public record ParticleData<T extends ParticleOption>(ParticleType<T> type, ParticleOption option) {

    public ParticleData(ParticleType<T> type) {
        this(type, null);
    }
}
