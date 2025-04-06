package com.artillexstudios.axapi.nms.v1_21_R1.packet;

import com.artillexstudios.axapi.packetentity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Location;

public class ClientboundTeleportEntityWrapper {

    public static ClientboundTeleportEntityPacket createNew(PacketEntity entity, Location location) {
        FriendlyByteBuf buf = PacketTransformer.newByteBuf().buf();
        buf.writeVarInt(entity.id());
        buf.writeDouble(location.getX());
        buf.writeDouble(location.getY());
        buf.writeDouble(location.getZ());
        buf.writeByte((byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        buf.writeByte((byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        buf.writeBoolean(true);
        return ClientboundTeleportEntityPacket.STREAM_CODEC.decode(buf);
    }
}
