package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.nms.wrapper.ServerWrapper;
import com.artillexstudios.axapi.nms.wrapper.WrapperRegistry;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;

public abstract class PacketWrapper {
    private Object cachedPacket;
    private boolean dirty;

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

    public void markDirty() {
        this.dirty = true;
    }

    public Object cached() {
        if (this.cachedPacket == null || this.dirty) {
            FriendlyByteBuf buf = FriendlyByteBuf.alloc();
            buf.writeVarInt(ClientboundPacketTypes.forPacketType(this.packetType()));
            this.write(buf);
            this.cachedPacket = ServerWrapper.INSTANCE.transformPacket(buf);
            this.dirty = false;
        }

        return this.cachedPacket;
    }

    public abstract PacketType packetType();
}
