package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.GeyserParticleOption;

public final class GeyserParticleType implements ParticleType<GeyserParticleOption> {

    @Override
    public void write(GeyserParticleOption data, FriendlyByteBuf buf) {
        buf.writeInt(data.waterBlocks());
    }

    @Override
    public GeyserParticleOption read(FriendlyByteBuf buf) {
        return new GeyserParticleOption(buf.readInt());
    }
}
