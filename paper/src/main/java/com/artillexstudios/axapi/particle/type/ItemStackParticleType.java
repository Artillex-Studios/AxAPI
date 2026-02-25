package com.artillexstudios.axapi.particle.type;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleType;
import com.artillexstudios.axapi.particle.option.ItemStackParticleOption;

public final class ItemStackParticleType implements ParticleType<ItemStackParticleOption> {

    @Override
    public void write(ItemStackParticleOption data, FriendlyByteBuf buf) {
        buf.writeItemStack(data.stack());
    }

    @Override
    public ItemStackParticleOption read(FriendlyByteBuf buf) {
        return new ItemStackParticleOption(buf.readItemStack());
    }
}
