package com.artillexstudios.axapi.packet.wrapper.serverbound;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.Version;

public class ServerboundSetCreativeModeSlotWrapper extends PacketWrapper {
    private int slot;
    private WrappedItemStack stack;

    public ServerboundSetCreativeModeSlotWrapper(PacketEvent event) {
        super(event);
    }

    public int slot() {
        return this.slot;
    }

    public void slot(int slot) {
        this.slot = slot;
    }

    public WrappedItemStack stack() {
        return this.stack;
    }

    public void stack(WrappedItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeShort(this.slot);
        out.writeItemStack(this.stack);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.slot = Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4) ? buf.readUnsignedShort() : buf.readShort();
        this.stack = buf.readItemStack();
    }

    @Override
    public PacketType packetType() {
        return ServerboundPacketTypes.SET_CREATIVE_MODE_SLOT;
    }
}
