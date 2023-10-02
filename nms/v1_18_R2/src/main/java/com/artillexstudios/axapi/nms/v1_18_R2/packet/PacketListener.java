package com.artillexstudios.axapi.nms.v1_18_R2.packet;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
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
        if (msg instanceof ServerboundInteractPacket packet) {
            try {
                FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
                packet.write(byteBuf);
                int entityId = byteBuf.readVarInt();
                int actionType = byteBuf.readVarInt();
                InteractionHand hand = null;
                Vector vector = null;
                boolean attack = false;
                if (actionType == 0) {
                    // Interact
                    int interactionHand = byteBuf.readVarInt();
                    hand = interactionHand == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                } else if (actionType == 1) {
                    // Attack
                    attack = true;
                } else {
                    // Interact at
                    float x = byteBuf.readFloat();
                    float y = byteBuf.readFloat();
                    float z = byteBuf.readFloat();
                    vector = new Vector(x, y, z);
                    int interactionHand = byteBuf.readVarInt();
                    hand = interactionHand == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                }

                byteBuf.release();

                PacketEntity entity = PacketEntityTracker.getById(entityId);
                if (entity != null) {
                    PacketEntityInteractEvent event = new PacketEntityInteractEvent(player, entity, attack, vector, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND);
                    com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketEntity packetEntity = (com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketEntity) entity;
                    packetEntity.acceptEventConsumers(event);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        super.channelRead(ctx, msg);
    }
}
