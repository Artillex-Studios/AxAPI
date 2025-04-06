package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.DustParticleOption;
import com.artillexstudios.axapi.utils.Colors;

public final class DustParticleType implements ParticleType<DustParticleOption> {

    @Override
    public void write(DustParticleOption data, FriendlyByteBuf buf) {
        buf.writeInt(Colors.fromVector(data.color()));
        buf.writeFloat(data.scale());
    }

    @Override
    public DustParticleOption read(FriendlyByteBuf buf) {
        return new DustParticleOption(Colors.toVector(buf.readInt()), buf.readFloat());
    }
}
