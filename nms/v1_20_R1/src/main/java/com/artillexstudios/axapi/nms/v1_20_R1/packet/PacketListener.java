package com.artillexstudios.axapi.nms.v1_20_R1.packet;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.hologram.HologramLine;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.hologram.impl.ComponentHologramLine;
import com.artillexstudios.axapi.nms.v1_20_R1.entity.EntityData;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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

                PacketEntity entity = AxPlugin.tracker.getById(entityId);
                if (entity != null) {
                    PacketEntityInteractEvent event = new PacketEntityInteractEvent(player, entity, attack, vector, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND);
                    com.artillexstudios.axapi.nms.v1_20_R1.entity.PacketEntity packetEntity = (com.artillexstudios.axapi.nms.v1_20_R1.entity.PacketEntity) entity;
                    packetEntity.acceptEventConsumers(event);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ClientboundSetEntityDataPacket dataPacket) {
            HologramLine<?> line = Holograms.byId(dataPacket.id());

            if (!(line instanceof ComponentHologramLine)) {
                // The entity is not a packet entity, skip!
                super.write(ctx, msg, promise);
                return;
            }

            List<SynchedEntityData.DataValue<?>> dataValues = dataPacket.packedItems();
            Iterator<SynchedEntityData.DataValue<?>> valueIterator = dataValues.iterator();

            SynchedEntityData.DataValue<?> value = null;
            while (valueIterator.hasNext()) {
                SynchedEntityData.DataValue<?> next = valueIterator.next();
                if (next.id() != EntityData.CUSTOM_NAME.getId()) continue;
                Optional<Component> content = (Optional<Component>) next.value();
                if (content.isEmpty()) return;

                String legacy = net.minecraft.network.chat.Component.Serializer.toJson(content.get());

                for (Placeholder placeholder : line.getPlaceholders()) {
                    legacy = placeholder.parse(player, legacy);
                }

                value = new SynchedEntityData.DataValue<>(next.id(), EntityData.CUSTOM_NAME.getSerializer(), Optional.ofNullable(net.minecraft.network.chat.Component.Serializer.fromJson(legacy)));
                valueIterator.remove();
                break;
            }

            if (value != null) {
                dataValues.add(value);
            }

            super.write(ctx, dataPacket, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}
