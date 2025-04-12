package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.ColorParticleOption;
import com.artillexstudios.axapi.utils.Colors;

public final class ColorParticleType implements ParticleType<ColorParticleOption> {

    @Override
    public void write(ColorParticleOption data, FriendlyByteBuf buf) {
        buf.writeInt(Colors.fromVector(data.color()));
    }

    @Override
    public ColorParticleOption read(FriendlyByteBuf buf) {
        return new ColorParticleOption(Colors.toVector(buf.readInt()));
    }
}
