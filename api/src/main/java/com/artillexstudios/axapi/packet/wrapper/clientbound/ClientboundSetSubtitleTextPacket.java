package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

public final class ClientboundSetSubtitleTextPacket extends PacketWrapper {

    public ClientboundSetSubtitleTextPacket(PacketEvent event) {
        super(event);
    }

    public ClientboundSetSubtitleTextPacket(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {

    }

    @Override
    public void read(FriendlyByteBuf buf) {

    }

    @Override
    public PacketType packetType() {
        return null;
    }
}
