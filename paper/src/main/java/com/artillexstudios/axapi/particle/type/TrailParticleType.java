package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.TrailParticleOption;
import com.artillexstudios.axapi.utils.Colors;

public final class TrailParticleType implements ParticleType<TrailParticleOption> {

    @Override
    public void write(TrailParticleOption data, FriendlyByteBuf buf) {
        buf.writeDouble(data.x());
        buf.writeDouble(data.y());
        buf.writeDouble(data.z());
        buf.writeInt(Colors.fromVector(data.color()));
        buf.writeVarInt(data.duration());
    }

    @Override
    public TrailParticleOption read(FriendlyByteBuf buf) {
        return new TrailParticleOption(buf.readDouble(), buf.readDouble(), buf.readDouble(), Colors.toVector(buf.readInt()), buf.readVarInt());
    }
}
