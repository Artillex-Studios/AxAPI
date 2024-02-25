package com.artillexstudios.axapi.nms.v1_20_R3.packet;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.hologram.HologramLine;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.hologram.impl.ComponentHologramLine;
import com.artillexstudios.axapi.nms.v1_20_R3.entity.EntityData;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacketListener extends ChannelDuplexHandler {
    private static final Cache<Component, String> legacyCache = Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterAccess(Duration.ofSeconds(2))
            .build();
    private static final Cache<String, Component> componentCache = Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterAccess(Duration.ofSeconds(2))
            .build();

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
                    com.artillexstudios.axapi.nms.v1_20_R3.entity.PacketEntity packetEntity = (com.artillexstudios.axapi.nms.v1_20_R3.entity.PacketEntity) entity;
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
            List<SynchedEntityData.DataValue<?>> newList = new ArrayList<>(dataValues.size());

            for (SynchedEntityData.DataValue<?> next : dataValues) {
                if (next.id() != EntityData.CUSTOM_NAME.getId()) {
                    newList.add(next);
                } else {
                    Optional<Component> content = (Optional<Component>) next.value();
                    if (content.isEmpty()) {
                        super.write(ctx, msg, promise);
                        return;
                    }

                    String legacy = legacyCache.get(content.get(), Component.Serializer::toJson);
                    if (legacy == null) {
                        super.write(ctx, msg, promise);
                        return;
                    }

                    for (Placeholder placeholder : line.getPlaceholders()) {
                        legacy = placeholder.parse(player, legacy);
                    }

                    SynchedEntityData.DataValue<?> value = new SynchedEntityData.DataValue<>(next.id(), EntityData.CUSTOM_NAME.getSerializer(), Optional.ofNullable(componentCache.get(legacy, Component.Serializer::fromJson)));
                    newList.add(value);
                }
            }

            dataValues.clear();
            dataValues.addAll(newList);

            super.write(ctx, dataPacket, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}
