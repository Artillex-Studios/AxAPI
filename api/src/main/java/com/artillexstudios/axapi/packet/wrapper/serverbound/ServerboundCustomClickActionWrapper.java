package com.artillexstudios.axapi.packet.wrapper.serverbound;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import net.kyori.adventure.key.Key;

public final class ServerboundCustomClickActionWrapper extends PacketWrapper {
    private static final int PAYLOAD_LIMIT = 65536;
    private Key key;
    private CompoundTag tag;

    public ServerboundCustomClickActionWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {
        this.key = out.readResourceLocation();
        FriendlyByteBuf buf = FriendlyByteBuf.alloc();
        buf.writeBoolean(this.tag != null);
        if (this.tag != null) {
            buf.writeNBT(this.tag);
        }
        int i = buf.readableBytes();
        if (i > PAYLOAD_LIMIT) {
            throw new IllegalArgumentException("Buffer size " + i + " is  larger than maximum of " + PAYLOAD_LIMIT + "!");
        }
        out.writeVarInt(i);
        out.writeBytes(buf);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.key);
        int length = buf.readVarInt();
        if (length > PAYLOAD_LIMIT) {
            throw new IllegalArgumentException("Buffer size " + length + " is larger than maximum of " + PAYLOAD_LIMIT + "!");
        }

        int i1 = buf.readerIndex();
        FriendlyByteBuf byteBuf = buf.slice(i1, length);
        boolean b = byteBuf.readBoolean();
        if (b) {
            this.tag = byteBuf.readNBT();
        }
        buf.readerIndex(i1 + length);
    }

    @Override
    public PacketType packetType() {
        return ServerboundPacketTypes.CUSTOM_CLICK_ACTION;
    }
}
