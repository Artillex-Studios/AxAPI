package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleOption;
import com.artillexstudios.axapi.particle.ParticleType;

public final class SimpleParticleType implements ParticleType<ParticleOption> {

    @Override
    public void write(ParticleOption data, FriendlyByteBuf buf) {

    }

    @Override
    public ParticleOption read(FriendlyByteBuf buf) {
        return null;
    }
}
