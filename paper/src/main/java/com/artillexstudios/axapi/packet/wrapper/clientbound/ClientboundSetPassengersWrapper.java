package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

public final class ClientboundSetPassengersWrapper extends PacketWrapper {
    private int vehicleId;
    private int[] passengers;

    public ClientboundSetPassengersWrapper(PacketEvent event) {
        super(event);
    }

    public ClientboundSetPassengersWrapper(int vehicleId, int[] passengers) {
        this.vehicleId = vehicleId;
        this.passengers = passengers;
    }

    public int[] passengers() {
        return this.passengers;
    }

    public void passengers(int[] passengers) {
        this.passengers = passengers;
        this.markDirty();
    }

    public int vehicleId() {
        return this.vehicleId;
    }

    public void vehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
        this.markDirty();
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeVarInt(this.vehicleId);
        out.writeVarIntArray(this.passengers);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.vehicleId = buf.readVarInt();
        this.passengers = buf.readVarIntArray();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.SET_PASSENGERS;
    }
}
