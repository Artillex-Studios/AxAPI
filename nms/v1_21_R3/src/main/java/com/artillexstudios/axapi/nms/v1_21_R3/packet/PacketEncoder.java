package com.artillexstudios.axapi.nms.v1_21_R3.packet;

import com.artillexstudios.axapi.nms.v1_21_R3.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class PacketEncoder extends MessageToMessageEncoder<ByteBuf> {
    private final ServerPlayerWrapper wrapper;

    public PacketEncoder(ServerPlayerWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable()) {
            list.add(byteBuf.retain());
            return;
        }

        int packetId = VarInt.read(byteBuf);
        PacketEvent event = new PacketEvent(this.wrapper.wrapped(), PacketSide.CLIENT_BOUND, ClientboundPacketTypes.forPacketId(packetId), () -> {
            return new FriendlyByteBufWrapper(new RegistryFriendlyByteBuf(byteBuf, MinecraftServer.getServer().registryAccess()));
        }, () -> {
            ByteBuf buf = channelHandlerContext.alloc().buffer();
            VarInt.write(buf, packetId);
            return new FriendlyByteBufWrapper(new RegistryFriendlyByteBuf(buf, MinecraftServer.getServer().registryAccess()));
        });
        PacketEvents.INSTANCE.callEvent(event);

        if (event.cancelled()) {
            return;
        }

        if (event.directOut() == null) {
            list.add(byteBuf.retain());
            return;
        }

        list.add(((FriendlyByteBufWrapper) event.directOut()).buf().retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtils.error("Exception!", cause);
        super.exceptionCaught(ctx, cause);
    }
}
