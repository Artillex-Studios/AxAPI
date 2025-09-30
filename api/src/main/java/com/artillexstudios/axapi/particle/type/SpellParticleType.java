package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.SpellParticleOption;

public final class SpellParticleType implements ParticleType<SpellParticleOption> {

    @Override
    public void write(SpellParticleOption data, FriendlyByteBuf buf) {
        buf.writeInt(data.color());
        buf.writeFloat(data.power());
    }

    @Override
    public SpellParticleOption read(FriendlyByteBuf buf) {
        return new SpellParticleOption(buf.readInt(), buf.readFloat());
    }
}
