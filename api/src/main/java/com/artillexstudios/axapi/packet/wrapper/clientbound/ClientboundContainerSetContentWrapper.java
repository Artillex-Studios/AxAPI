package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class ClientboundContainerSetContentWrapper extends PacketWrapper {
    private int containerId;
    private int stateId;
    private List<WrappedItemStack> items;
    private WrappedItemStack carriedItem;

    public ClientboundContainerSetContentWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeContainerId(this.containerId);
        out.writeVarInt(this.stateId);
        out.writeVarInt(this.items.size());
        for (WrappedItemStack item : this.items) {
            out.writeItemStack(item);
        }
        out.writeItemStack(this.carriedItem == null ? WrappedItemStack.wrap(new ItemStack(Material.AIR)) : this.carriedItem);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.containerId = buf.readContainerId();
        this.stateId = buf.readVarInt();
        int count = buf.readVarInt();
        this.items = new ObjectArrayList<>(count);
        for (int i = 0; i < count; i++) {
            items.add(buf.readItemStack());
        }
        this.carriedItem = buf.readItemStack();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.CONTAINER_CONTENT;
    }

    public int containerId() {
        return this.containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int stateId() {
        return this.stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public List<WrappedItemStack> items() {
        return this.items;
    }

    public void setItems(List<WrappedItemStack> items) {
        this.items = items;
    }

    public WrappedItemStack carriedItem() {
        return this.carriedItem;
    }

    public void setCarriedItem(WrappedItemStack carriedItem) {
        this.carriedItem = carriedItem;
    }
}
