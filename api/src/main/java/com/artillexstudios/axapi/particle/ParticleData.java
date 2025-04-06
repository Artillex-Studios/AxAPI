package com.artillexstudios.axapi.particle;

public record ParticleData<T extends ParticleOption>(ParticleType<T> type, ParticleOption option) {
}
