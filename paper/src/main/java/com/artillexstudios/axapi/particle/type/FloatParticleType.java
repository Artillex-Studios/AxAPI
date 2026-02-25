package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.FloatParticleOption;

public final class FloatParticleType implements ParticleType<FloatParticleOption> {

    @Override
    public void write(FloatParticleOption data, FriendlyByteBuf buf) {
        buf.writeFloat(data.value());
    }

    @Override
    public FloatParticleOption read(FriendlyByteBuf buf) {
        return new FloatParticleOption(buf.readFloat());
    }
}
