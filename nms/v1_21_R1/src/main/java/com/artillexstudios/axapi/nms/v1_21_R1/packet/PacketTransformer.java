package com.artillexstudios.axapi.nms.v1_21_R1.packet;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.IdDispatchCodec;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;
import java.util.function.Function;

public final class PacketTransformer {
    private static final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private static final StreamCodec<ByteBuf, Packet<? super ClientGamePacketListener>> clientboundCodec = GameProtocols.CLIENTBOUND_TEMPLATE.bind(decorator).codec();
    private static final StreamCodec<ByteBuf, Packet<? super ServerGamePacketListener>> serverboundCodec = GameProtocols.SERVERBOUND_TEMPLATE.bind(decorator).codec();
    private static final FieldAccessor toIdAccessor = FieldAccessor.builder()
            .withClass(IdDispatchCodec.class)
            .withField("d")
            .build();
    private static final Object2IntMap<PacketType> clientboundIds = toIdAccessor.getUnchecked(clientboundCodec);
    private static final Object2IntMap<PacketType> serverboundIds = toIdAccessor.getUnchecked(serverboundCodec);

    public static Packet<?> transformClientbound(PacketWrapper wrapper) {
        FriendlyByteBufWrapper buf = new FriendlyByteBufWrapper(decorator.apply(Unpooled.buffer()));
        wrapper.write(buf);
        return transformClientbound(buf.buf());
    }

    public static Packet<?> transformClientbound(FriendlyByteBuf buf) {
        return transformClientbound(((FriendlyByteBufWrapper) buf).buf());
    }

    public static Packet<? super ClientGamePacketListener> transformClientbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return clientboundCodec.decode(buf);
        } finally {
            buf.release();
        }
    }

    public static Packet<? super ServerGamePacketListener> transformServerbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return serverboundCodec.decode(buf);
        } finally {
            buf.release();
        }
    }

    public static Packet<?> transformServerbound(PacketWrapper wrapper) {
        FriendlyByteBufWrapper buf = new FriendlyByteBufWrapper(decorator.apply(Unpooled.buffer()));
        wrapper.write(buf);
        return transformServerbound0(buf.buf());
    }

    public static Packet<?> transformServerbound(FriendlyByteBuf buf) {
        return transformServerbound0(((FriendlyByteBufWrapper) buf).buf());
    }

    private static Packet<?> transformServerbound0(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return serverboundCodec.decode(buf);
        } finally {
            buf.release();
        }
    }

    public static FriendlyByteBufWrapper transformServerbound(ChannelHandlerContext ctx, Packet<?> packet) {
        return transformServerbound(ctx, packet, buf -> {
        });
    }

    public static FriendlyByteBufWrapper transformServerbound(Packet<?> packet) {
        return transformServerbound(null, packet, buf -> {
        });
    }

    public static FriendlyByteBufWrapper transformServerbound(Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        return transformServerbound(null, packet, consumer);
    }

    public static FriendlyByteBufWrapper transformServerbound(ChannelHandlerContext ctx, Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        ByteBuf buffer = alloc(ctx);
        serverboundCodec.encode(buffer, (Packet<? super ServerGamePacketListener>) packet);
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(decorator.apply(buffer));
        consumer.accept(wrapper);
        return wrapper;
    }

    public static FriendlyByteBufWrapper transformClientbound(ChannelHandlerContext ctx, Packet<?> packet) {
        return transformClientbound(ctx, packet, buf -> {
        });
    }

    public static FriendlyByteBufWrapper transformClientbound(Packet<?> packet) {
        return transformClientbound(null, packet, buf -> {
        });
    }

    public static FriendlyByteBufWrapper transformClientbound(Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        return transformClientbound(null, packet, consumer);
    }

    public static FriendlyByteBufWrapper transformClientbound(ChannelHandlerContext ctx, Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        ByteBuf buffer = alloc(ctx);
        clientboundCodec.encode(buffer, (Packet<? super ClientGamePacketListener>) packet);
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(decorator.apply(buffer));
        consumer.accept(wrapper);
        return wrapper;
    }

    public static FriendlyByteBufWrapper newByteBuf() {
        return newByteBuf(null, buf -> {
        });
    }

    public static FriendlyByteBufWrapper newByteBuf(ChannelHandlerContext ctx) {
        return newByteBuf(ctx, buf -> {
        });
    }

    public static FriendlyByteBufWrapper newByteBuf(Consumer<FriendlyByteBuf> consumer) {
        return newByteBuf(null, consumer);
    }

    public static FriendlyByteBufWrapper newByteBuf(ChannelHandlerContext ctx, Consumer<FriendlyByteBuf> consumer) {
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(decorator.apply(alloc(ctx)));
        consumer.accept(wrapper);
        return wrapper;
    }

    public static ByteBuf alloc() {
        return alloc(null);
    }

    public static ByteBuf alloc(ChannelHandlerContext ctx) {
        return ctx == null ? Unpooled.buffer() : ctx.alloc().buffer();
    }

    public static int packetId(Packet<?> packet) {
        net.minecraft.network.protocol.PacketType<?> type = packet.type();
        int packetId;
        if (type.flow() == PacketFlow.CLIENTBOUND) {
            packetId = clientboundIds.getOrDefault(type, -1);
        } else {
            packetId = serverboundIds.getOrDefault(type, -1);
        }

        if (packetId == -1) {
            throw new IllegalStateException();
        }

        return packetId;
    }

    public static FriendlyByteBufWrapper wrap(ByteBuf buf) {
        return new FriendlyByteBufWrapper(decorator.apply(buf));
    }

    public static FriendlyByteBufWrapper copy(FriendlyByteBufWrapper friendlyByteBufWrapper) {
        return wrap(friendlyByteBufWrapper.buf().copy());
    }
}
