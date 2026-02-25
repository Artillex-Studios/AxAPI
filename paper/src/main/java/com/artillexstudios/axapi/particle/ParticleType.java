package com.artillexstudios.axapi.particle;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;

public interface ParticleType<T extends ParticleOption> {

    void write(T data, FriendlyByteBuf buf);

    T read(FriendlyByteBuf buf);
}
