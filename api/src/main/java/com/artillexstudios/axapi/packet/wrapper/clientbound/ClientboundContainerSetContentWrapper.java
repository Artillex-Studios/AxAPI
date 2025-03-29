package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.shared.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.shared.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.shared.axapi.packet.PacketEvent;
import com.artillexstudios.shared.axapi.packet.PacketType;
import com.artillexstudios.shared.axapi.packet.wrapper.PacketWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class ClientboundContainerSetContentWrapper extends PacketWrapper {
    private int containerId;
    private int stateId;
    private List<ItemStack> items;
    private ItemStack carriedItem;

    public ClientboundContainerSetContentWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeContainerId(this.containerId);
        out.writeVarInt(this.stateId);
        out.writeVarInt(this.items.size());
        for (ItemStack item : this.items) {
            out.writeItemStack(item);
        }
        out.writeItemStack(this.carriedItem == null ? new ItemStack(Material.AIR) : this.carriedItem);
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

    public List<ItemStack> items() {
        return this.items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public ItemStack carriedItem() {
        return this.carriedItem;
    }

    public void setCarriedItem(ItemStack carriedItem) {
        this.carriedItem = carriedItem;
    }
}
