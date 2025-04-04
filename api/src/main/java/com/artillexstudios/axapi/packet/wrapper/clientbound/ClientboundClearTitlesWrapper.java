package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

public final class ClientboundClearTitlesWrapper extends PacketWrapper {
    private boolean reset;

    public ClientboundClearTitlesWrapper(boolean reset) {
        this.reset = reset;
    }

    public ClientboundClearTitlesWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeBoolean(this.reset);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.reset = buf.readBoolean();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.CLEAR_TITLES;
    }
}
