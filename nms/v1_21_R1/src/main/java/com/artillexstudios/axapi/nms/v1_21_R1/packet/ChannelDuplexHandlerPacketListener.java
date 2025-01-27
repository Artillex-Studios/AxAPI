package com.artillexstudios.axapi.nms.v1_21_R1.packet;

import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.PacketTypes;
import com.artillexstudios.axapi.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ChannelDuplexHandlerPacketListener extends ChannelDuplexHandler {
    private final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private final StreamCodec<ByteBuf, Packet<? super ClientGamePacketListener>> codec = GameProtocols.CLIENTBOUND_TEMPLATE.bind(decorator).codec();
    private final Player player;

    public ChannelDuplexHandlerPacketListener(Player player) {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        LogUtils.info("Sending packet! {}", msg);
        if (!PacketEvents.INSTANCE.listening()) {
            LogUtils.info("Not listening!");
            super.write(ctx, msg, promise);
            return;
        }

        if (msg instanceof ClientboundBundlePacket bundlePacket) {
            LogUtils.info("Bundle packet");
            List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
            for (Packet<? super ClientGamePacketListener> subPacket : bundlePacket.subPackets()) {
                ByteBuf buf = ctx.alloc().buffer();
                codec.encode(buf, subPacket);
                int packetId = VarInt.read(buf);
                LogUtils.info("(bundle) Packet id: {}, class: {}", packetId, subPacket.getClass());


                PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, packetId, () -> new FriendlyByteBufWrapper(decorator.apply(buf)), () -> {
                    ByteBuf out = ctx.alloc().buffer();
                    VarInt.write(out, packetId);
                    return new FriendlyByteBufWrapper(decorator.apply(out));
                });

                PacketEvents.INSTANCE.callEvent(event);
                if (event.cancelled()) {
                    continue;
                }

                // Nothing was changed
                FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
                if (out == null) {
                    packets.add(subPacket);
                    continue;
                }

                packets.add(codec.decode(out.buf()));
            }

            super.write(ctx, new ClientboundBundlePacket(packets), promise);
            return;
        }

        ByteBuf buf = ctx.alloc().buffer();
        codec.encode(buf, (Packet<? super ClientGamePacketListener>) msg);
        int packetId = VarInt.read(buf);
        PacketType type = PacketTypes.forPacketId(packetId);
        LogUtils.info("Packet id: {}, class: {}", packetId, msg.getClass());

        PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> new FriendlyByteBufWrapper(decorator.apply(buf)), () -> {
            ByteBuf out = ctx.alloc().buffer();
            VarInt.write(out, packetId);
            return new FriendlyByteBufWrapper(decorator.apply(out));
        });

        PacketEvents.INSTANCE.callEvent(event);
        if (event.cancelled()) {
            LogUtils.info("Cancelled event!");
            return;
        }

        // Nothing was changed
        FriendlyByteBufWrapper out = (FriendlyByteBufWrapper) event.directOut();
        if (out == null) {
            LogUtils.info("Unchanged!");
            super.write(ctx, msg, promise);
            return;
        }

        LogUtils.info("Changed!");
        super.write(ctx, codec.decode(out.buf()), promise);
    }
}
