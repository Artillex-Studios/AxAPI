package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

public final class ClientboundContainerSetSlotWrapper extends PacketWrapper {
    private byte containerId;
    private int stateId;
    private short slot;
    private WrappedItemStack stack;

    public ClientboundContainerSetSlotWrapper(PacketEvent event) {
        super(event);
    }

    public WrappedItemStack stack() {
        return this.stack;
    }

    public void stack(WrappedItemStack stack) {
        this.stack = stack;
    }

    public short slot() {
        return this.slot;
    }

    public void slot(short slot) {
        this.slot = slot;
    }

    public int stateId() {
        return this.stateId;
    }

    public void stateId(int stateId) {
        this.stateId = stateId;
    }

    public byte containerId() {
        return this.containerId;
    }

    public void containerId(byte containerId) {
        this.containerId = containerId;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeByte(this.containerId);
        out.writeVarInt(this.stateId);
        out.writeShort(this.slot);
        out.writeItemStack(this.stack);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.containerId = buf.readByte();
        this.stateId = buf.readVarInt();
        this.slot = buf.readShort();
        this.stack = buf.readItemStack();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.CONTAINER_SET_SLOT;
    }
}
