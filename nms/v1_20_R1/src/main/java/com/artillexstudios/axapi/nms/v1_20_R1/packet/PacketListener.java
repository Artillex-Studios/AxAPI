package com.artillexstudios.axapi.nms.v1_20_R1.packet;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class PacketListener extends ChannelDuplexHandler {
    private final Player player;

    public PacketListener(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        System.out.println("Channel read! " + msg.getClass());
        if (msg instanceof ServerboundInteractPacket packet) {
            try {
                FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
                packet.write(byteBuf);
                System.out.println(byteBuf);

//                int entityId = byteBuf.readVarInt();
//                ServerboundInteractPacket.ActionType type = byteBuf.readEnum(ServerboundInteractPacket.ActionType.class);
//                InteractionHand hand = null;
//                Vector vector = null;
//                boolean attack = false;
//                if (type == ServerboundInteractPacket.ActionType.INTERACT) {
//                    hand = byteBuf.readEnum(InteractionHand.class);
//                } else if (type == ServerboundInteractPacket.ActionType.INTERACT_AT) {
//                    vector = new Vector(byteBuf.readFloat(), byteBuf.readFloat(), byteBuf.readFloat());
//                    hand = byteBuf.readEnum(InteractionHand.class);
//                } else {
//                    attack = true;
//                }
//                byteBuf.clear();
//
//                PacketEntity entity = PacketEntityTracker.getById(entityId);
//                if (entity != null) {
//                    Bukkit.getPluginManager().callEvent(new PacketEntityInteractEvent(player, entity, attack, vector, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND));
//                }
                System.out.println("Interact packet");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        super.channelRead(ctx, msg);
    }
}
