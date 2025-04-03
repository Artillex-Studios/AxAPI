package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

public final class ClientboundSetActionBarTextWrapper extends PacketWrapper {
    private Component text;

    public ClientboundSetActionBarTextWrapper(Component text) {
        super(null);
        this.text = text;
    }

    public ClientboundSetActionBarTextWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeComponent(this.text);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.text = buf.readComponent();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.ACTION_BAR;
    }
}
