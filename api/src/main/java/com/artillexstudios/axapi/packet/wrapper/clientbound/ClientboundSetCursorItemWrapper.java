package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

public final class ClientboundSetCursorItemWrapper extends PacketWrapper {
    private WrappedItemStack itemStack;

    public ClientboundSetCursorItemWrapper(WrappedItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ClientboundSetCursorItemWrapper(PacketEvent event) {
        super(event);
    }

    public WrappedItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(WrappedItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeItemStack(this.itemStack);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.itemStack = buf.readItemStack();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.SET_CURSOR_ITEM;
    }
}
