package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;

public abstract class PacketWrapper {

    public PacketWrapper() {
        this(null);
    }

    public PacketWrapper(PacketEvent event) {
        if (event != null) {
            FriendlyByteBuf buf = event.in();
            if (buf == null) {
                return;
            }

            event.setWrapper(this);
            this.read(buf);
        }
    }

    public abstract void write(FriendlyByteBuf out);

    public abstract void read(FriendlyByteBuf buf);

    public abstract PacketType packetType();
}
