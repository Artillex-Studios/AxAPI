package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;

public final class ClientboundContainerSetSlotWrapper extends PacketWrapper {
    private final FriendlyByteBuf out;
    private byte containerId;
    private int stateId;
    private short slot;
    private WrappedItemStack stack;

    public ClientboundContainerSetSlotWrapper(PacketEvent event) {
        event.setWrapper(this);
        FriendlyByteBuf buf = event.in();
        this.out = event.out();
        this.containerId = buf.readByte();
        this.stateId = buf.readVarInt();
        this.slot = buf.readShort();
        this.stack = buf.readItemStack();
    }

    public WrappedItemStack stack() {
        return stack;
    }

    public void stack(WrappedItemStack stack) {
        this.stack = stack;
    }

    public short slot() {
        return slot;
    }

    public void slot(short slot) {
        this.slot = slot;
    }

    public int stateId() {
        return stateId;
    }

    public void stateId(int stateId) {
        this.stateId = stateId;
    }

    public byte containerId() {
        return containerId;
    }

    public void containerId(byte containerId) {
        this.containerId = containerId;
    }

    @Override
    public void write() {
        this.out.writeByte(this.containerId);
        this.out.writeVarInt(this.stateId);
        this.out.writeShort(this.slot);
        this.out.writeItemStack(this.stack);
    }
}
