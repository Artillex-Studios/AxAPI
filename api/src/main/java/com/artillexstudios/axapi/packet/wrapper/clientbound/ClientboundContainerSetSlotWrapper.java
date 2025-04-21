package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.Version;

public final class ClientboundContainerSetSlotWrapper extends PacketWrapper {
    private int containerId;
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

    public int containerId() {
        return this.containerId;
    }

    public void containerId(byte containerId) {
        this.containerId = containerId;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeContainerId(this.containerId);
        out.writeVarInt(this.stateId);
        out.writeShort(this.slot);
        out.writeItemStack(this.stack);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.containerId = Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_2) ? buf.readContainerId() : buf.readByte();
        this.stateId = buf.readVarInt();
        this.slot = buf.readShort();
        this.stack = buf.readItemStack();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.CONTAINER_SET_SLOT;
    }
}
