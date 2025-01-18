package com.artillexstudios.axapi.nms.v1_21_R1.packet;

import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

public final class ClientboundPacketListener extends MessageToByteEncoder<Packet<?>> {
    private final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private final StreamCodec<ByteBuf, Packet<? super ClientGamePacketListener>> codec = GameProtocols.CLIENTBOUND_TEMPLATE.bind(decorator).codec();
    private final Player player;

    public ClientboundPacketListener(Player player) {
        this.player = player;
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) {
        return true;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf) {
        this.handlePacket(channelHandlerContext, packet, byteBuf);
        RegistryFriendlyByteBuf in = decorator.apply(channelHandlerContext.alloc().buffer());
        LogUtils.info("Encode called! Buf: {} Packet: {}, class: {}", byteBuf, packet, packet.getClass());
        if (packet instanceof ClientboundBundlePacket bundlePacket) {

        }

        codec.encode(in, (Packet<? super ClientGamePacketListener>) packet);
        LogUtils.info("After write! {}", in);
        RegistryFriendlyByteBuf out = decorator.apply(byteBuf);
        out.writeVarInt(in.readVarInt());
//        PacketEvent event = new PacketEvent(this.player, new FriendlyByteBufWrapper(in), new FriendlyByteBufWrapper(out));
//
//        if (event.cancelled()) {
//            out.clear();
//            LogUtils.info("Cancelled!");
//        } else if (!event.handled()) {
//            out.clear();
//            codec.encode(out, (Packet<? super ClientGamePacketListener>) packet);
//        }
    }

    public boolean handlePacket(ChannelHandlerContext context, Packet<?> packet, ByteBuf byteBuf) {
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
            for (Packet<? super ClientGamePacketListener> sub : bundlePacket.subPackets()) {

            }
        }

        return true;
    }
}
