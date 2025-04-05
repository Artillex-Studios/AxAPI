package com.artillexstudios.axapi.nms.v1_21_R4.packet;

import com.artillexstudios.axapi.nms.v1_21_R4.wrapper.ServerPlayerWrapper;
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
    private final StreamCodec<ByteBuf, Packet<? super ServerGamePacketListener>> serverboundCodec;
    private static final FieldAccessor toIdAccessor = FieldAccessor.builder()
            .withClass(IdDispatchCodec.class)
            .withField("d")
            .build();
    private static final Object2IntMap<PacketType> clientboundIds = toIdAccessor.getUnchecked(clientboundCodec);
    private final Object2IntMap<PacketType> serverboundIds;

    public PacketTransformer(ServerPlayerWrapper wrapper) {
        this.serverboundCodec = GameProtocols.SERVERBOUND_TEMPLATE.bind(decorator, wrapper.asMinecraft().connection).codec();
        this.serverboundIds = toIdAccessor.getUnchecked(serverboundCodec);
    }

    public Packet<?> transformClientbound(PacketWrapper wrapper) {
        FriendlyByteBufWrapper buf = new FriendlyByteBufWrapper(decorator.apply(Unpooled.buffer()));
        wrapper.write(buf);
        return transformClientbound(buf.buf());
    }

    public Packet<?> transformClientbound(FriendlyByteBuf buf) {
        return transformClientbound(((FriendlyByteBufWrapper) buf).buf());
    }

    public Packet<? super ClientGamePacketListener> transformClientbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return clientboundCodec.decode(buf);
        } finally {
            buf.release();
        }
    }

    public Packet<? super ServerGamePacketListener> transformServerbound(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return serverboundCodec.decode(buf);
        } finally {
            buf.release();
        }
    }

    public Packet<?> transformServerbound(PacketWrapper wrapper) {
        FriendlyByteBufWrapper buf = new FriendlyByteBufWrapper(decorator.apply(Unpooled.buffer()));
        wrapper.write(buf);
        return transformServerbound0(buf.buf());
    }

    public Packet<?> transformServerbound(FriendlyByteBuf buf) {
        return transformServerbound0(((FriendlyByteBufWrapper) buf).buf());
    }

    private Packet<?> transformServerbound0(net.minecraft.network.FriendlyByteBuf buf) {
        try {
            return serverboundCodec.decode(buf);
        } finally {
            buf.release();
        }
    }

    public FriendlyByteBuf transformServerbound(ChannelHandlerContext ctx, Packet<?> packet) {
        return transformServerbound(ctx, packet, buf -> {
        });
    }

    public FriendlyByteBuf transformServerbound(Packet<?> packet) {
        return transformServerbound(null, packet, buf -> {
        });
    }

    public FriendlyByteBuf transformServerbound(Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        return transformServerbound(null, packet, consumer);
    }

    public FriendlyByteBuf transformServerbound(ChannelHandlerContext ctx, Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        ByteBuf buffer = alloc(ctx);
        serverboundCodec.encode(buffer, (Packet<? super ServerGamePacketListener>) packet);
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(decorator.apply(buffer));
        consumer.accept(wrapper);
        return wrapper;
    }

    public FriendlyByteBuf transformClientbound(ChannelHandlerContext ctx, Packet<?> packet) {
        return transformClientbound(ctx, packet, buf -> {
        });
    }

    public FriendlyByteBuf transformClientbound(Packet<?> packet) {
        return transformClientbound(null, packet, buf -> {
        });
    }

    public FriendlyByteBuf transformClientbound(Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        return transformClientbound(null, packet, consumer);
    }

    public FriendlyByteBuf transformClientbound(ChannelHandlerContext ctx, Packet<?> packet, Consumer<FriendlyByteBuf> consumer) {
        ByteBuf buffer = alloc(ctx);
        clientboundCodec.encode(buffer, (Packet<? super ClientGamePacketListener>) packet);
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(decorator.apply(buffer));
        consumer.accept(wrapper);
        return wrapper;
    }

    public FriendlyByteBuf newByteBuf() {
        return newByteBuf(null, buf -> {
        });
    }

    public FriendlyByteBuf newByteBuf(ChannelHandlerContext ctx) {
        return newByteBuf(ctx, buf -> {
        });
    }

    public FriendlyByteBuf newByteBuf(Consumer<FriendlyByteBuf> consumer) {
        return newByteBuf(null, consumer);
    }

    public FriendlyByteBuf newByteBuf(ChannelHandlerContext ctx, Consumer<FriendlyByteBuf> consumer) {
        FriendlyByteBufWrapper wrapper = new FriendlyByteBufWrapper(decorator.apply(alloc(ctx)));
        consumer.accept(wrapper);
        return wrapper;
    }

    public ByteBuf alloc() {
        return alloc(null);
    }

    public ByteBuf alloc(ChannelHandlerContext ctx) {
        return ctx == null ? Unpooled.buffer() : ctx.alloc().buffer();
    }

    public int packetId(Packet<?> packet) {
        net.minecraft.network.protocol.PacketType<?> type = packet.type();
        int packetId;
        if (type.flow() == PacketFlow.CLIENTBOUND) {
            packetId = clientboundIds.getOrDefault(type, -1);
        } else {
            packetId = this.serverboundIds.getOrDefault(type, -1);
        }

        if (packetId == -1) {
            throw new IllegalStateException();
        }

        return packetId;
    }

    public static FriendlyByteBuf wrap(ByteBuf buf) {
        return new FriendlyByteBufWrapper(decorator.apply(buf));
    }

    public static FriendlyByteBuf copy(FriendlyByteBufWrapper friendlyByteBufWrapper) {
        return wrap(friendlyByteBufWrapper.buf().copy());
    }
}
