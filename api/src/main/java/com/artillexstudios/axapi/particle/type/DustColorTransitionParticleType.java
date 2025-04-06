package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.DustColorTransitionParticleOption;
import com.artillexstudios.axapi.utils.Colors;

public final class DustColorTransitionParticleType implements ParticleType<DustColorTransitionParticleOption> {

    @Override
    public void write(DustColorTransitionParticleOption data, FriendlyByteBuf buf) {
        buf.writeInt(Colors.fromVector(data.color1()));
        buf.writeInt(Colors.fromVector(data.color2()));
        buf.writeFloat(data.scale());
    }

    @Override
    public DustColorTransitionParticleOption read(FriendlyByteBuf buf) {
        return new DustColorTransitionParticleOption(Colors.toVector(buf.readInt()), Colors.toVector(buf.readInt()), buf.readFloat());
    }
}
