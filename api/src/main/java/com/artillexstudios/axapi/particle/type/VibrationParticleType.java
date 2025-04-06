package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.VibrationParticleOption;

public final class VibrationParticleType implements ParticleType<VibrationParticleOption> {

    @Override
    public void write(VibrationParticleOption data, FriendlyByteBuf buf) {
        buf.writeVarInt(data.source());
        buf.writeBlockPos(data.position());
        buf.writeVarInt(data.entityId());
        buf.writeFloat(data.eyeHeight());
        buf.writeVarInt(data.ticks());
    }

    @Override
    public VibrationParticleOption read(FriendlyByteBuf buf) {
        return new VibrationParticleOption(buf.readVarInt(), buf.readBlockPosition(), buf.readVarInt(), buf.readFloat(), buf.readVarInt());
    }
}
