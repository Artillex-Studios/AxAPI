package com.artillexstudios.axapi.nms.v1_21_R2.packet;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.IdDispatchCodec;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ChannelDuplexHandlerPacketListener extends ChannelDuplexHandler {
    private static final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private static final StreamCodec<ByteBuf, Packet<? super ClientGamePacketListener>> clientboundCodec = GameProtocols.CLIENTBOUND_TEMPLATE.bind(decorator).codec();
    private static final StreamCodec<ByteBuf, Packet<? super ServerGamePacketListener>> serverboundCodec = GameProtocols.SERVERBOUND_TEMPLATE.bind(decorator).codec();
    private static final FieldAccessor toIdAccessor = FieldAccessor.builder()
            .withClass(IdDispatchCodec.class)
            .withField("d")
            .build();
    private static final Object2IntMap<PacketType> clientboundIds = toIdAccessor.getUnchecked(clientboundCodec);
    private static final Object2IntMap<PacketType> serverboundIds = toIdAccessor.getUnchecked(serverboundCodec);
    private final FeatureFlags flags;
    private final Player player;

    public ChannelDuplexHandlerPacketListener(FeatureFlags flags, Player player) {
        this.flags = flags;
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!PacketEvents.INSTANCE.listening()) {
            super.write(ctx, msg, promise);
            return;
        }

        if (msg instanceof ClientboundBundlePacket bundlePacket) {
            if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                LogUtils.info("Bundle packet");
            }
            List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
            for (Packet<? super ClientGamePacketListener> subPacket : bundlePacket.subPackets()) {
                int packetId = packetId(subPacket);
                PacketType type = ClientboundPacketTypes.forPacketId(packetId);
                if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                    LogUtils.info("(bundle) Packet id: {}, class: {}, type: {}", packetId, subPacket.getClass(), type);
                }

                PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> {
                    ByteBuf buf = ctx.alloc().buffer();
                    clientboundCodec.encode(buf, subPacket);
                    VarInt.read(buf);
                    return new FriendlyByteBufWrapper(decorator.apply(buf));
                }, () -> {
                    ByteBuf out = ctx.alloc().buffer();
                    VarInt.write(out, packetId);
                    return new FriendlyByteBufWrapper(decorator.apply(out));
                });

                PacketEvents.INSTANCE.callEvent(event);
                if (event.cancelled()) {
                    FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
                    if (out != null) {
                        out.buf().release();
                    }

                    FriendlyByteBufWrapper in = (FriendlyByteBufWrapper) event.directIn();
                    if (in != null) {
                        in.buf().release();
                    }

                    continue;
                }

                // Nothing was changed
                FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();

                FriendlyByteBufWrapper in = (FriendlyByteBufWrapper) event.directIn();
                if (in != null) {
                    in.buf().release();
                }

                if (out == null) {
                    packets.add(subPacket);
                    continue;
                }

                packets.add(clientboundCodec.decode(out.buf()));
                out.buf().release();
            }

            super.write(ctx, new ClientboundBundlePacket(packets), promise);
            return;
        }

        int packetId = packetId((Packet<?>) msg);
        PacketType type = ClientboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
            LogUtils.info("Packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> {
            ByteBuf buf = ctx.alloc().buffer();
            clientboundCodec.encode(buf, (Packet<? super ClientGamePacketListener>) msg);
            VarInt.read(buf);
            return new FriendlyByteBufWrapper(decorator.apply(buf));
        }, () -> {
            ByteBuf out = ctx.alloc().buffer();
            VarInt.write(out, packetId);
            return new FriendlyByteBufWrapper(decorator.apply(out));
        });

        PacketEvents.INSTANCE.callEvent(event);
        if (event.cancelled()) {
            if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                LogUtils.info("Cancelled event!");
            }

            FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
            if (out != null) {
                out.buf().release();
            }

            FriendlyByteBufWrapper in = (FriendlyByteBufWrapper) event.directIn();
            if (in != null) {
                in.buf().release();
            }
            return;
        }

        // Nothing was changed
        FriendlyByteBufWrapper in = (FriendlyByteBufWrapper) event.directIn();
        if (in != null) {
            in.buf().release();
        }

        FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
        if (out == null) {
            if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                LogUtils.info("Unchanged!");
            }
            super.write(ctx, msg, promise);
            return;
        }

        if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
            LogUtils.info("Changed!");
        }
        super.write(ctx, clientboundCodec.decode(out.buf()), promise);
        out.buf().release();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!PacketEvents.INSTANCE.listening()) {
            super.channelRead(ctx, msg);
            return;
        }

        if (msg instanceof ServerboundCustomPayloadPacket(
                net.minecraft.network.protocol.common.custom.CustomPacketPayload payload
        )) {
            super.channelRead(ctx, msg);
            return;
        }

        int packetId = packetId((Packet<?>) msg);
        PacketType type = ServerboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
            LogUtils.info("Incoming packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.SERVER_BOUND, type, () -> {
            ByteBuf buf = ctx.alloc().buffer();
            serverboundCodec.encode(buf, (Packet<? super ServerGamePacketListener>) msg);
            VarInt.read(buf);
            return new FriendlyByteBufWrapper(decorator.apply(buf));
        }, () -> {
            ByteBuf out = ctx.alloc().buffer();
            VarInt.write(out, packetId);
            return new FriendlyByteBufWrapper(decorator.apply(out));
        });

        PacketEvents.INSTANCE.callEvent(event);
        if (event.cancelled()) {
            if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
                LogUtils.info("Incoming cancelled event!");
            }
            FriendlyByteBufWrapper in = (FriendlyByteBufWrapper) event.directIn();
            if (in != null) {
                in.buf().release();
            }

            FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
            if (out != null) {
                out.buf().release();
            }
            return;
        }

        // Nothing was changed
        FriendlyByteBufWrapper in = (FriendlyByteBufWrapper) event.directIn();
        if (in != null) {
            in.buf().release();
        }

        FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
        if (out == null) {
            if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
                LogUtils.info("Incoming unchanged!");
            }
            super.channelRead(ctx, msg);
            return;
        }

        if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
            LogUtils.info("Incoming changed!");
        }
        super.channelRead(ctx, serverboundCodec.decode(out.buf()));
        out.buf().release();
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
}
