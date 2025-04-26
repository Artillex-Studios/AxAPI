package com.artillexstudios.axapi.nms.v1_20_R2.packet;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
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

        if (!(msg instanceof Packet<?>)) {
            super.write(ctx, msg, promise);
            return;
        }

        if (msg instanceof ClientboundBundlePacket bundlePacket) {
            if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                LogUtils.info("Bundle packet");
            }
            List<Packet<ClientGamePacketListener>> packets = new ArrayList<>();
            for (Packet<ClientGamePacketListener> subPacket : bundlePacket.subPackets()) {
                int packetId = PacketTransformer.packetId(PacketSide.CLIENT_BOUND, subPacket);
                if (packetId == -1) {
                    super.write(ctx, msg, promise);
                    return;
                }

                PacketType type = ClientboundPacketTypes.forPacketId(packetId);
                if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                    LogUtils.info("(bundle) Packet id: {}, class: {}, type: {}", packetId, subPacket.getClass(), type);
                }

                PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> {
                    try {
                        FriendlyByteBuf buf = PacketTransformer.transformClientbound(ctx, subPacket, fbb -> {});
                        if (buf == null) {
                            super.write(ctx, msg, promise);
                            return null;
                        }

                        return buf;
                    } catch (Exception exception) {
                        try {
                            super.write(ctx, msg, promise);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    }
                }, () -> {
                    return PacketTransformer.newByteBuf(ctx, buf -> {
                        buf.writeVarInt(packetId);
                    });
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

                packets.add(PacketTransformer.transformClientbound(out.buf()));
            }

            super.write(ctx, new ClientboundBundlePacket(packets), promise);
            return;
        }

        int packetId = PacketTransformer.packetId(PacketSide.CLIENT_BOUND, msg);
        if (packetId == -1) {
            super.write(ctx, msg, promise);
            return;
        }

        PacketType type = ClientboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
            LogUtils.info("Packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> {
            try {
                FriendlyByteBuf buf = PacketTransformer.transformClientbound(ctx, msg, fbb -> {});
                if (buf == null) {
                    super.write(ctx, msg, promise);
                    return null;
                }

                return buf;
            } catch (Exception exception) {
                try {
                    super.write(ctx, msg, promise);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        }, () -> {
            return PacketTransformer.newByteBuf(ctx, buf -> {
                buf.writeVarInt(packetId);
            });
        });

        try {
            PacketEvents.INSTANCE.callEvent(event);
        } catch (RuntimeException e) {
            LogUtils.info("Packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }
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
        super.write(ctx, PacketTransformer.transformClientbound(out.buf()), promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!PacketEvents.INSTANCE.listening()) {
            super.channelRead(ctx, msg);
            return;
        }

        if (!(msg instanceof Packet<?>)) {
            super.channelRead(ctx, msg);
            return;
        }

        if (msg instanceof ServerboundCustomPayloadPacket(
                net.minecraft.network.protocol.common.custom.CustomPacketPayload payload
        )) {
            super.channelRead(ctx, msg);
            return;
        }

        int packetId = PacketTransformer.packetId(PacketSide.SERVER_BOUND, msg);
        if (packetId == -1) {
            super.channelRead(ctx, msg);
            return;
        }

        PacketType type = ServerboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
            LogUtils.info("Incoming packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.SERVER_BOUND, type, () -> {
            try {
                FriendlyByteBuf buf = PacketTransformer.transformServerbound(ctx, msg, fbb -> {});
                if (buf == null) {
                    super.channelRead(ctx, msg);
                    return null;
                }

                return buf;
            } catch (Exception exception) {
                try {
                    super.channelRead(ctx, msg);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        }, () -> {
            return PacketTransformer.newByteBuf(ctx, buf -> {
                buf.writeVarInt(packetId);
            });
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
        super.channelRead(ctx, PacketTransformer.transformServerbound(out.buf()));
    }
}
