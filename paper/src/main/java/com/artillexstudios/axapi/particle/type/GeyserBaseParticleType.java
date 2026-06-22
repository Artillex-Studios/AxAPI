package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.GeyserBaseParticleOption;

public final class GeyserBaseParticleType implements ParticleType<GeyserBaseParticleOption> {

    @Override
    public void write(GeyserBaseParticleOption data, FriendlyByteBuf buf) {
        buf.writeInt(data.waterBlocks());
        buf.writeFloat(data.burstImpulseBase());
    }

    @Override
    public GeyserBaseParticleOption read(FriendlyByteBuf buf) {
        return new GeyserBaseParticleOption(buf.readInt(), buf.readFloat());
    }
}
