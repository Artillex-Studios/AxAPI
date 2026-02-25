package com.artillexstudios.axapi.packet.wrapper.serverbound;

import com.artillexstudios.axapi.items.HashedStack;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class ServerboundContainerClickWrapper extends PacketWrapper {
    private int containerId;
    private int stateId;
    private short slotNum;
    private byte buttonNum;
    private ClickType clickType;
    private Int2ObjectMap<HashedStack> changedSlots;
    private HashedStack carriedItem;

    public ServerboundContainerClickWrapper(PacketEvent event) {
        super(event);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getStateId() {
        return this.stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public short getSlotNum() {
        return this.slotNum;
    }

    public void setSlotNum(short slotNum) {
        this.slotNum = slotNum;
    }

    public byte getButtonNum() {
        return this.buttonNum;
    }

    public void setButtonNum(byte buttonNum) {
        this.buttonNum = buttonNum;
    }

    public ClickType getClickType() {
        return this.clickType;
    }

    public void setClickType(ClickType clickType) {
        this.clickType = clickType;
    }

    public Int2ObjectMap<HashedStack> getChangedSlots() {
        return this.changedSlots;
    }

    public void setChangedSlots(Int2ObjectMap<HashedStack> changedSlots) {
        this.changedSlots = changedSlots;
    }

    public HashedStack getCarriedItem() {
        return this.carriedItem;
    }

    public void setCarriedItem(HashedStack carriedItem) {
        this.carriedItem = carriedItem;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeContainerId(this.containerId);
        out.writeVarInt(this.stateId);
        out.writeShort(this.slotNum);
        out.writeByte(this.buttonNum);
        out.writeEnum(this.clickType);
        int size = this.changedSlots.size();
        if (size > 128) {
            throw new RuntimeException();
        }

        out.writeVarInt(size);
        this.changedSlots.forEach((slot, stack) -> {
            out.writeShort(slot.shortValue());
            out.writeHashedStack(stack);
        });
        out.writeHashedStack(this.carriedItem);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.containerId = buf.readContainerId();
        this.stateId = buf.readVarInt();
        this.slotNum = buf.readShort();
        this.buttonNum = buf.readByte();
        this.clickType = buf.readEnum(ClickType.class);
        int size = buf.readVarInt();
        this.changedSlots = new Int2ObjectOpenHashMap<>();
        for (int i = 0; i < size; i++) {
            this.changedSlots.put(buf.readShort(), buf.readHashedStack());
        }
        this.carriedItem = buf.readHashedStack();
    }

    @Override
    public PacketType packetType() {
        return ServerboundPacketTypes.CONTAINER_CLICK;
    }

    public enum ClickType {
        PICKUP,
        QUICK_MOVE,
        SWAP,
        CLONE,
        THROW,
        QUICK_CRAFT,
        PICKUP_ALL;
    }
}
