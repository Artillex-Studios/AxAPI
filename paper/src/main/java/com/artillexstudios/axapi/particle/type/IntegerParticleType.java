package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.IntegerParticleOption;

public final class IntegerParticleType implements ParticleType<IntegerParticleOption> {

    @Override
    public void write(IntegerParticleOption data, FriendlyByteBuf buf) {
        buf.writeInt(data.value());
    }

    @Override
    public IntegerParticleOption read(FriendlyByteBuf buf) {
        return new IntegerParticleOption(buf.readInt());
    }
}
