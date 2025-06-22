package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;

public final class ClientboundSetPassengersWrapper extends PacketWrapper {
    private int vehicleId;
    private List<Integer> passengers;

    public ClientboundSetPassengersWrapper(PacketEvent event) {
        super(event);
    }

    public ClientboundSetPassengersWrapper(int vehicleId, List<Integer> passengers) {
        this.vehicleId = vehicleId;
        this.passengers = passengers;
    }

    public List<Integer> passengers() {
        return this.passengers;
    }

    public void passengers(List<Integer> passengers) {
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
        out.writeVarInt(this.passengers.size());
        for (int passenger : this.passengers) {
            out.writeVarInt(passenger);
        }
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.vehicleId = buf.readVarInt();
        int readableBytes = buf.readableBytes();
        int length = buf.readVarInt();
        if (length > readableBytes) {
            throw new IllegalStateException();
        }

        List<Integer> ints = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ints.add(buf.readVarInt());
        }
        this.passengers = ints;
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.SET_PASSENGERS;
    }
}
