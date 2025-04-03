package com.artillexstudios.axapi.nms.v1_20_R1.packet;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class ChannelDuplexHandlerPacketListener extends ChannelDuplexHandler {
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
            List<Packet<ClientGamePacketListener>> packets = new ArrayList<>();
            for (Packet<ClientGamePacketListener> subPacket : bundlePacket.subPackets()) {
                int packetId = packetId(PacketSide.CLIENT_BOUND, subPacket);
                PacketType type = ClientboundPacketTypes.forPacketId(packetId);
                if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                    LogUtils.info("(bundle) Packet id: {}, class: {}, type: {}", packetId, subPacket.getClass(), type);
                }

                PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> {
                    ByteBuf buf = ctx.alloc().buffer();
                    FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(buf);
                    subPacket.write(friendlyByteBuf);
                    return new FriendlyByteBufWrapper(friendlyByteBuf);
                }, () -> {
                    ByteBuf out = ctx.alloc().buffer();
                    return new FriendlyByteBufWrapper(new FriendlyByteBuf(out));
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

                packets.add((Packet<ClientGamePacketListener>) ConnectionProtocol.PLAY.createPacket(PacketFlow.CLIENTBOUND, packetId, out.buf()));
                out.buf().release();
            }

            super.write(ctx, new ClientboundBundlePacket(packets), promise);
            return;
        }

        int packetId = packetId(PacketSide.CLIENT_BOUND, (Packet<?>) msg);
        PacketType type = ClientboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
            LogUtils.info("Packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> {
            ByteBuf buf = ctx.alloc().buffer();
            FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(buf);
            ((Packet<?>) msg).write(friendlyByteBuf);
            return new FriendlyByteBufWrapper(friendlyByteBuf);
        }, () -> {
            ByteBuf out = ctx.alloc().buffer();
            return new FriendlyByteBufWrapper(new FriendlyByteBuf(out));
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
        super.write(ctx, ConnectionProtocol.PLAY.createPacket(PacketFlow.CLIENTBOUND, packetId, out.buf()), promise);
        out.buf().release();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!PacketEvents.INSTANCE.listening()) {
            super.channelRead(ctx, msg);
            return;
        }

        if (msg instanceof ServerboundCustomPayloadPacket) {
            super.channelRead(ctx, msg);
            return;
        }

        int packetId = packetId(PacketSide.SERVER_BOUND, (Packet<?>) msg);
        PacketType type = ServerboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
            LogUtils.info("Incoming packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.SERVER_BOUND, type, () -> {
            ByteBuf buf = ctx.alloc().buffer();
            FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(buf);
            ((Packet<?>) msg).write(friendlyByteBuf);
            return new FriendlyByteBufWrapper(friendlyByteBuf);
        }, () -> {
            ByteBuf out = ctx.alloc().buffer();
            return new FriendlyByteBufWrapper(new FriendlyByteBuf(out));
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
        super.channelRead(ctx, ConnectionProtocol.PLAY.createPacket(PacketFlow.SERVERBOUND, packetId, out.buf()));
        out.buf().release();
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
}
