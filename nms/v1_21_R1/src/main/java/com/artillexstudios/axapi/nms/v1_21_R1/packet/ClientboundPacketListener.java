package com.artillexstudios.axapi.nms.v1_21_R1.packet;

import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.PacketTypes;
import com.artillexstudios.axapi.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;

import java.util.function.Function;

public final class ClientboundPacketListener extends MessageToByteEncoder<ByteBuf> {
    private final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private final Player player;

    public ClientboundPacketListener(Player player) {
        this.player = player;
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) {
        return true;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (!msg.isReadable()) {
            LogUtils.info("Unreadable message!");
            return;
        }

        ByteBuf changed = ctx.alloc().buffer().writeBytes(msg);
        try {
            int packetId = VarInt.read(changed);
            PacketType type = PacketTypes.forPacketId(packetId);
            LogUtils.info("Type: {}", type);

            PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, type, () -> new FriendlyByteBufWrapper(decorator.apply(msg)), () -> new FriendlyByteBufWrapper(decorator.apply(changed)));
            PacketEvents.INSTANCE.callEvent(event);
            if (event.cancelled()) {
                changed.clear();
            }

            changed.resetReaderIndex();
            out.writeBytes(changed);
            LogUtils.info("Out: {}", out);
        } finally {
            changed.release();
        }
    }
}
