package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.artillexstudios.axapi.utils.Pair;
import com.google.common.collect.Lists;

import java.util.List;

public final class ClientboundSetEquipmentWrapper extends PacketWrapper {
    private int entityId;
    private List<Pair<EquipmentSlot, WrappedItemStack>> items;

    public ClientboundSetEquipmentWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeVarInt(this.entityId);
        int size = this.items.size();
        for (int i = 0; i < size; i++) {
            Pair<EquipmentSlot, WrappedItemStack> slot = this.items.get(i);
            boolean last = i != size - 1;
            out.writeByte(last ? slot.first().ordinal() | -128 : slot.first().ordinal());
            out.writeItemStack(slot.second());
        }
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.items = Lists.newArrayList();

        int _byte;
        do {
            _byte = buf.readByte();
            EquipmentSlot slot = EquipmentSlot.values()[_byte & 127];
            WrappedItemStack wrappedItemStack = buf.readItemStack();
            this.items.add(new Pair<>(slot, wrappedItemStack));
        } while ((_byte & -128) != 0);
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.SET_EQUIPMENT;
    }

    public int entityId() {
        return this.entityId;
    }

    public void entityId(int entityId) {
        this.entityId = entityId;
    }

    public List<Pair<EquipmentSlot, WrappedItemStack>> items() {
        return this.items;
    }

    public void items(List<Pair<EquipmentSlot, WrappedItemStack>> items) {
        this.items = items;
    }
}
