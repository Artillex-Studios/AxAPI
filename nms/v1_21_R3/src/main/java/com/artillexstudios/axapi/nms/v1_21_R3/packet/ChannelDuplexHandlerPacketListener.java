package com.artillexstudios.axapi.nms.v1_21_R3.packet;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.artillexstudios.shared.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.shared.axapi.packet.PacketEvent;
import com.artillexstudios.shared.axapi.packet.PacketEvents;
import com.artillexstudios.shared.axapi.packet.PacketSide;
import com.artillexstudios.shared.axapi.packet.PacketType;
import com.artillexstudios.shared.axapi.packet.ServerboundPacketTypes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ChannelDuplexHandlerPacketListener extends ChannelDuplexHandler {
    private static final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private static final StreamCodec<ByteBuf, Packet<? super ClientGamePacketListener>> clientboundCodec = GameProtocols.CLIENTBOUND_TEMPLATE.bind(decorator).codec();
    private static final StreamCodec<ByteBuf, Packet<? super ServerGamePacketListener>> serverboundCodec = GameProtocols.SERVERBOUND_TEMPLATE.bind(decorator).codec();
    private final FeatureFlags flags;
    private final Player player;
    private final PacketEvents events = Bukkit.getServicesManager().getRegistration(PacketEvents.class).getProvider();

    public ChannelDuplexHandlerPacketListener(FeatureFlags flags, Player player) {
        this.flags = flags;
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!events.listening()) {
            super.write(ctx, msg, promise);
            return;
        }

        if (msg instanceof ClientboundBundlePacket bundlePacket) {
            if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                LogUtils.info("Bundle packet");
            }
            List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
            for (Packet<? super ClientGamePacketListener> subPacket : bundlePacket.subPackets()) {
                ByteBuf buf = ctx.alloc().buffer();
                clientboundCodec.encode(buf, subPacket);
                int packetId = VarInt.read(buf);
                PacketType type = ClientboundPacketTypes.forPacketId(packetId);
                if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                    LogUtils.info("(bundle) Packet id: {}, class: {}, type: {}", packetId, subPacket.getClass(), type);
                }

                PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> new FriendlyByteBufWrapper(decorator.apply(buf)), () -> {
                    ByteBuf out = ctx.alloc().buffer();
                    VarInt.write(out, packetId);
                    return new FriendlyByteBufWrapper(decorator.apply(out));
                });

                events.callEvent(event);
                if (event.cancelled()) {
                    continue;
                }

                // Nothing was changed
                FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
                if (out == null) {
                    packets.add(subPacket);
                    continue;
                }

                packets.add(clientboundCodec.decode(out.buf()));
            }

            super.write(ctx, new ClientboundBundlePacket(packets), promise);
            return;
        }

        ByteBuf buf = ctx.alloc().buffer();
        clientboundCodec.encode(buf, (Packet<? super ClientGamePacketListener>) msg);
        // TODO: We might want to use the packetType of the packet, but I'd assume that check to be more expensive (key comparsion)
        int packetId = VarInt.read(buf);
        PacketType type = ClientboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
            LogUtils.info("Packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> new FriendlyByteBufWrapper(decorator.apply(buf)), () -> {
            ByteBuf out = ctx.alloc().buffer();
            VarInt.write(out, packetId);
            return new FriendlyByteBufWrapper(decorator.apply(out));
        });

        events.callEvent(event);
        if (event.cancelled()) {
            if (this.flags.DEBUG_OUTGOING_PACKETS.get()) {
                LogUtils.info("Cancelled event!");
            }
            return;
        }

        // Nothing was changed
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
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!events.listening()) {
            super.channelRead(ctx, msg);
            return;
        }

        if (msg instanceof ServerboundCustomPayloadPacket(
                net.minecraft.network.protocol.common.custom.CustomPacketPayload payload
        )) {
            super.channelRead(ctx, msg);
            return;
        }

        ByteBuf buf = ctx.alloc().buffer();
        serverboundCodec.encode(buf, (Packet<? super ServerGamePacketListener>) msg);
        int packetId = VarInt.read(buf);
        PacketType type = ServerboundPacketTypes.forPacketId(packetId);
        if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
            LogUtils.info("Incoming packet id: {}, class: {}, type: {}", packetId, msg.getClass(), type);
        }

        PacketEvent event = new PacketEvent(this.player, PacketSide.SERVER_BOUND, type, () -> new FriendlyByteBufWrapper(decorator.apply(buf)), () -> {
            ByteBuf out = ctx.alloc().buffer();
            VarInt.write(out, packetId);
            return new FriendlyByteBufWrapper(decorator.apply(out));
        });

        events.callEvent(event);
        if (event.cancelled()) {
            if (this.flags.DEBUG_INCOMING_PACKETS.get()) {
                LogUtils.info("Incoming cancelled event!");
            }
            return;
        }

        // Nothing was changed
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
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
