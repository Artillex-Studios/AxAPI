package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.position.BlockPosition;
import com.artillexstudios.axapi.utils.position.ImmutableBlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public final class ClientboundBlockUpdateWrapper extends PacketWrapper {
    private BlockPosition position;
    private BlockData blockData;

    public ClientboundBlockUpdateWrapper(Location position, Material material) {
        this.position = new ImmutableBlockPosition(position.getBlockX(), position.getBlockY(), position.getBlockZ());
        this.blockData = material.createBlockData();
    }

    public ClientboundBlockUpdateWrapper(BlockPosition position, Material material) {
        this.position = position.immutable();
        this.blockData = material.createBlockData();
    }

    public ClientboundBlockUpdateWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeBlockPos(this.position);
        out.writeBlockData(this.blockData);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.position = buf.readBlockPosition();
        this.blockData = buf.readBlockData();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.BLOCK_UPDATE;
    }
}
