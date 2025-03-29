package com.artillexstudios.axapi.nms.v1_21_R1.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;

public class ClientboundSetPassengersWrapper {

    public static ClientboundSetPassengersPacket createNew(int ridingEntity, int[] passengerIds) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(ridingEntity);
        buf.writeVarIntArray(passengerIds);
        ClientboundSetPassengersPacket packet = ClientboundSetPassengersPacket.STREAM_CODEC.decode(buf);
        buf.release();
        return packet;
    }
}
