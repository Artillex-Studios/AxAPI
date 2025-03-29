package com.artillexstudios.shared.axapi.packet.wrapper;

import com.artillexstudios.shared.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.shared.axapi.packet.PacketEvent;
import com.artillexstudios.shared.axapi.packet.PacketType;

public abstract class PacketWrapper {

    public PacketWrapper(PacketEvent event) {
        if (event != null) {
            event.setWrapper(this);
            this.read(event.in());
        }
    }

    public abstract void write(FriendlyByteBuf out);

    public abstract void read(FriendlyByteBuf buf);

    public abstract PacketType packetType();
}
