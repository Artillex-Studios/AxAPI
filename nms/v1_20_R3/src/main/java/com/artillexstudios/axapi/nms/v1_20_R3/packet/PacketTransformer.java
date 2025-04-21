package com.artillexstudios.axapi.nms.v1_20_R3.packet;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.VarInt;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

import java.util.function.Consumer;

public final class PacketTransformer {
    private static final FieldAccessor packetSetAccessor = FieldAccessor.builder()
            .withClass(ConnectionProtocol.CodecData.class)
            .withField("c")
            .build();
    private static final FieldAccessor classToIdAccessor = FieldAccessor.builder()
            .withClass("net.minecraft.network.EnumProtocol$b")
            .withField("b")
            .build();
    private static final ConnectionProtocol.CodecData<?> clientboundCodec = ConnectionProtocol.PLAY.codec(PacketFlow.CLIENTBOUND);
    private static final ConnectionProtocol.CodecData<?> serverboundCodec = ConnectionProtocol.PLAY.codec(PacketFlow.SERVERBOUND);

    public static Packet<?> transformClientbound(PacketWrapper wrapper) {
        FriendlyByteBufWrapper buf = new FriendlyByteBufWrapper(new net.minecraft.network.FriendlyByteBuf(Unpooled.buffer()));
        buf.writeVarInt(ClientboundPacketTypes.forPacketType(wrapper.packetType()));
        wrapper.write(buf);
        return transformClientbound(buf.buf());
    }

    public static Packet<?> transformClientbound(FriendlyByteBuf buf) {
        return transformClientbound(((FriendlyByteBufWrapper) buf).buf());
    }

    public static Packet<ClientGamePacketListener> transformClientbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return (Packet<ClientGamePacketListener>) clientboundCodec.createPacket(buf.readVarInt(), buf);
        } finally {
            buf.release();
        }
    }

    public static Packet<ServerGamePacketListener> transformServerbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return (Packet<ServerGamePacketListener>) serverboundCodec.createPacket(buf.readVarInt(), buf);
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
            return serverboundCodec.createPacket(buf.readVarInt(), buf);
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

    public static FriendlyByteBufWrapper transformServerbound(ChannelHandlerContext ctx, Object packet, Consumer<FriendlyByteBuf> consumer) {
        FriendlyByteBufWrapper wrapper;
        if (packet instanceof Packet<?> packetObject) {
            net.minecraft.network.FriendlyByteBuf buffer = new net.minecraft.network.FriendlyByteBuf(alloc(ctx));
            packetObject.write(buffer);
            wrapper = new FriendlyByteBufWrapper(buffer);
        } else if (packet instanceof ByteBuf buffer) {
            wrapper = new FriendlyByteBufWrapper(new net.minecraft.network.FriendlyByteBuf(buffer.copy()));
            wrapper.readVarInt();
        } else {
            LogUtils.error("Unhandled packet class: {} Pipeline: {}", packet.getClass(), ctx.channel().pipeline().names());
            return null;
        }

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

    public static FriendlyByteBufWrapper transformClientbound(ChannelHandlerContext ctx, Object packet, Consumer<FriendlyByteBuf> consumer) {
        FriendlyByteBufWrapper wrapper;
        if (packet instanceof Packet<?> packetObject) {
            net.minecraft.network.FriendlyByteBuf buffer = new net.minecraft.network.FriendlyByteBuf(alloc(ctx));
            packetObject.write(buffer);
            wrapper = new FriendlyByteBufWrapper(buffer);
        } else if (packet instanceof ByteBuf buffer) {
            wrapper = new FriendlyByteBufWrapper(new net.minecraft.network.FriendlyByteBuf(buffer.copy()));
            wrapper.readVarInt();
        } else {
            LogUtils.error("Unhandled packet class: {} Pipeline: {}", packet.getClass(), ctx.channel().pipeline().names());
            return null;
        }

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

    public static int packetId(PacketSide side, Object input) {
        int packetId;
        if (input instanceof Packet<?> packet) {
            if (side == PacketSide.CLIENT_BOUND) {
                packetId = clientboundCodec.packetId(packet);
            } else {
                packetId = serverboundCodec.packetId(packet);
            }

            if (packetId == -1) {
                Class<?> clazz = input.getClass();
                while (true) {
                    clazz = clazz.getSuperclass();
                    if (clazz == null || clazz == Object.class) {
                        break;
                    }

                    Object packetSet;
                    if (side == PacketSide.CLIENT_BOUND) {
                        packetSet = packetSetAccessor.get(clientboundCodec);
                    } else {
                        packetSet = packetSetAccessor.get(serverboundCodec);
                    }

                    Object2IntMap<Class<? extends Packet<?>>> packets = classToIdAccessor.getUnchecked(packetSet);
                    packetId = packets.getInt(clazz);

                    if (packetId != -1) {
                        break;
                    }
                }

                return packetId;
            }
        } else if (input instanceof ByteBuf buffer) {
            int readerIndex = buffer.readerIndex();
            int writerIndex = buffer.writerIndex();
            packetId = VarInt.read(buffer);
            buffer.readerIndex(readerIndex);
            buffer.writerIndex(writerIndex);
        } else {
            LogUtils.error("Unhandled packet class: {}", input.getClass());
            packetId = -1;
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
