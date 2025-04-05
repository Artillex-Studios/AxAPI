package com.artillexstudios.axapi.nms.v1_20_R1.packet;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

import java.util.function.Consumer;

public final class PacketTransformer {
    public static Packet<?> transformClientbound(PacketWrapper wrapper) {
        FriendlyByteBufWrapper buf = new FriendlyByteBufWrapper(new net.minecraft.network.FriendlyByteBuf(Unpooled.buffer()));
        wrapper.write(buf);
        return transformClientbound(buf.buf());
    }

    public static Packet<?> transformClientbound(FriendlyByteBuf buf) {
        return transformClientbound(((FriendlyByteBufWrapper) buf).buf());
    }

    public static Packet<ClientGamePacketListener> transformClientbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return (Packet<ClientGamePacketListener>) ConnectionProtocol.PLAY.createPacket(PacketFlow.CLIENTBOUND, buf.readVarInt(), buf);
        } finally {
            buf.release();
        }
    }

    public static Packet<ServerGamePacketListener> transformServerbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return (Packet<ServerGamePacketListener>) ConnectionProtocol.PLAY.createPacket(PacketFlow.SERVERBOUND, buf.readVarInt(), buf);
        } finally {
            buf.release();
        }
    }

    public static Packet<?> transformServerbound(PacketWrapper wrapper) {
        FriendlyByteBufWrapper buf = new FriendlyByteBufWrapper(new net.minecraft.network.FriendlyByteBuf(Unpooled.buffer()));
        wrapper.write(buf);
        return transformServerbound0(buf.buf());
    }

    public static Packet<?> transformServerbound(FriendlyByteBuf buf) {
        return transformServerbound0(((FriendlyByteBufWrapper) buf).buf());
    }

    private static Packet<?> transformServerbound0(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return ConnectionProtocol.PLAY.createPacket(PacketFlow.SERVERBOUND, buf.readVarInt(), buf);
        } finally {
            buf.release();
        }
    }

    public static FriendlyByteBuf transformServerbound(ChannelHandlerContext ctx, Packet<?> packet) {
        return transformServerbound(ctx, packet, buf -> {
        });
    }

    public static FriendlyByteBuf transformServerbound(Packet<?> packet) {
        return transformServerbound(null, packet, buf -> {
        });
    }

    public static FriendlyByteBuf transformServerbound(Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        return transformServerbound(null, packet, consumer);
    }

    public static FriendlyByteBuf transformServerbound(ChannelHandlerContext ctx, Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        net.minecraft.network.FriendlyByteBuf buffer = new net.minecraft.network.FriendlyByteBuf(alloc(ctx));
        packet.write(buffer);
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(buffer);
        consumer.accept(wrapper);
        return wrapper;
    }

    public static FriendlyByteBuf transformClientbound(ChannelHandlerContext ctx, Packet<?> packet) {
        return transformClientbound(ctx, packet, buf -> {
        });
    }

    public static FriendlyByteBuf transformClientbound(Packet<?> packet) {
        return transformClientbound(null, packet, buf -> {
        });
    }

    public static FriendlyByteBuf transformClientbound(Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        return transformClientbound(null, packet, consumer);
    }

    public static FriendlyByteBuf transformClientbound(ChannelHandlerContext ctx, Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        net.minecraft.network.FriendlyByteBuf buffer = new net.minecraft.network.FriendlyByteBuf(alloc(ctx));
        packet.write(buffer);
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(buffer);
        consumer.accept(wrapper);
        return wrapper;
    }

    public static FriendlyByteBuf newByteBuf() {
        return newByteBuf(null, buf -> {
        });
    }

    public static FriendlyByteBuf newByteBuf(ChannelHandlerContext ctx) {
        return newByteBuf(ctx, buf -> {
        });
    }

    public static FriendlyByteBuf newByteBuf(Consumer<FriendlyByteBuf> consumer) {
        return newByteBuf(null, consumer);
    }

    public static FriendlyByteBuf newByteBuf(ChannelHandlerContext ctx, Consumer<FriendlyByteBuf> consumer) {
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(new net.minecraft.network.FriendlyByteBuf(alloc(ctx)));
        consumer.accept(wrapper);
        return wrapper;
    }

    public static ByteBuf alloc() {
        return alloc(null);
    }

    public static ByteBuf alloc(ChannelHandlerContext ctx) {
        return ctx == null ? Unpooled.buffer() : ctx.alloc().buffer();
    }

    public static int packetId(PacketSide side, Packet<?> packet) {
        int packetId;
        if (side == PacketSide.CLIENT_BOUND) {
            packetId = ConnectionProtocol.PLAY.getPacketId(PacketFlow.CLIENTBOUND, packet);
        } else {
            packetId = ConnectionProtocol.PLAY.getPacketId(PacketFlow.SERVERBOUND, packet);
        }

        if (packetId == -1) {
            throw new IllegalStateException();
        }

        return packetId;
    }

    public static FriendlyByteBuf wrap(ByteBuf buf) {
        return new FriendlyByteBufWrapper(new net.minecraft.network.FriendlyByteBuf(buf));
    }

    public static FriendlyByteBuf copy(FriendlyByteBufWrapper friendlyByteBufWrapper) {
        return wrap(friendlyByteBufWrapper.buf().copy());
    }
}
