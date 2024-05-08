package com.artillexstudios.axapi.nms.v1_18_R2.packet;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.collections.ThreadSafeList;
import com.artillexstudios.axapi.entity.impl.PacketEntity;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.hologram.HologramLine;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.items.PacketItemModifier;
import com.artillexstudios.axapi.nms.v1_18_R2.entity.EntityData;
import com.artillexstudios.axapi.nms.v1_18_R2.items.WrappedItemStack;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import com.artillexstudios.axapi.utils.placeholder.StaticPlaceholder;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PacketListener extends ChannelDuplexHandler {
    private static final Cache<Component, String> legacyCache = Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterAccess(Duration.ofSeconds(20))
            .scheduler(Scheduler.systemScheduler())
            .build();
    private static final Cache<String, Component> componentCache = Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterAccess(Duration.ofSeconds(20))
            .scheduler(Scheduler.systemScheduler())
            .build();
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();
    private static final Logger log = LoggerFactory.getLogger(PacketListener.class);

    private final Player player;

    public PacketListener(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if (msg instanceof ServerboundInteractPacket packet) {
            if (AxPlugin.tracker == null) {
                super.channelRead(ctx, msg);
                return;
            }

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
                com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketEntity packetEntity = (com.artillexstudios.axapi.nms.v1_18_R2.entity.PacketEntity) entity;
                packetEntity.acceptEventConsumers(event);
                Bukkit.getPluginManager().callEvent(event);
            }
        } else if (msg instanceof ServerboundSignUpdatePacket updatePacket) {
            SignInput signInput = SignInput.remove(player);
            if (signInput == null) {
                super.channelRead(ctx, msg);
                return;
            }


            signInput.getListener().accept(player, WrappedItemStack.asAdventureFromJson(Arrays.asList(updatePacket.getLines())).toArray(new net.kyori.adventure.text.Component[0]));
            com.artillexstudios.axapi.scheduler.Scheduler.get().run(task -> {
                CraftBlockData data = (CraftBlockData) signInput.getLocation().getBlock().getType().createBlockData();
                BlockPos pos = new BlockPos(signInput.getLocation().getBlockX(), signInput.getLocation().getBlockY(), signInput.getLocation().getBlockZ());
                ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, data.getState()));
            });
            return;
        }  else if (msg instanceof ServerboundSetCreativeModeSlotPacket packet) {
            var item = packet.getItem();
            if (PacketItemModifier.isListening()) {
                PacketItemModifier.restore(new WrappedItemStack(item));
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ClientboundSetEntityDataPacket dataPacket) {
            HologramLine line = Holograms.byId(dataPacket.getId());

            if (line == null || (line.type() != HologramLine.Type.TEXT || !line.hasPlaceholders())) {
                // The entity is not a packet entity, skip!
                super.write(ctx, msg, promise);
                return;
            }

            List<SynchedEntityData.DataItem<?>> dataValues = new ArrayList<>(dataPacket.getUnpackedData());
            Iterator<SynchedEntityData.DataItem<?>> iterator = dataValues.iterator();

            SynchedEntityData.DataItem<?> value = null;
            while (iterator.hasNext()) {
                SynchedEntityData.DataItem<?> next = iterator.next();
                if (next.getAccessor().getId() != EntityData.CUSTOM_NAME.getId()) continue;

                Optional<Component> content = (Optional<Component>) next.getValue();
                if (content.isEmpty()) {
                    super.write(ctx, msg, promise);
                    return;
                }

                String legacy = legacyCache.get(content.get(), (minecraftComponent) -> {
                    String gsonText = Component.Serializer.toJson(minecraftComponent);
                    net.kyori.adventure.text.Component gsonComponent = GsonComponentSerializer.gson().deserialize(gsonText);
                    return LEGACY_COMPONENT_SERIALIZER.serialize(gsonComponent);
                });

                if (legacy == null) {
                    super.write(ctx, msg, promise);
                    return;
                }

                ThreadSafeList<Placeholder> placeholders = line.placeholders();
                for (int i = 0; i < placeholders.size(); i++) {
                    Placeholder placeholder = placeholders.get(i);
                    if (placeholder instanceof StaticPlaceholder) continue;
                    legacy = placeholder.parse(player, legacy);
                }

                Component component = componentCache.get(legacy, (legacyText) -> {
                    net.kyori.adventure.text.Component formatted = StringUtils.format(legacyText);
                    String gson = GsonComponentSerializer.gson().serialize(formatted);
                    return Component.Serializer.fromJson(gson);
                });

                value = new SynchedEntityData.DataItem<>(EntityData.CUSTOM_NAME, Optional.ofNullable(component));
                iterator.remove();
                break;
            }

            if (value != null) {
                dataValues.add(value);
            }

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(dataPacket.getId());
            SynchedEntityData.pack(dataValues, buf);

            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(buf);
            super.write(ctx, packet, promise);

            buf.release();
        } else if (msg instanceof ClientboundContainerSetSlotPacket packet) {
            if (PacketItemModifier.isListening()) {
                PacketItemModifier.callModify(new WrappedItemStack(packet.getItem()), player, PacketItemModifier.Context.SET_SLOT);
            }

            super.write(ctx, packet, promise);
        } else if (msg instanceof ClientboundContainerSetContentPacket packet) {
            if (PacketItemModifier.isListening()) {
                PacketItemModifier.callModify(new WrappedItemStack(packet.getCarriedItem()), player, PacketItemModifier.Context.SET_CONTENTS);

                for (ItemStack item : packet.getItems()) {
                    PacketItemModifier.callModify(new WrappedItemStack(item), player, PacketItemModifier.Context.SET_CONTENTS);
                }
            }

            super.write(ctx, packet, promise);
        } else if (msg instanceof ClientboundSetEquipmentPacket packet) {
            if (PacketItemModifier.isListening()) {
                List<Pair<net.minecraft.world.entity.EquipmentSlot, ItemStack>> items = new ArrayList<>();

                for (Pair<net.minecraft.world.entity.EquipmentSlot, ItemStack> slot : packet.getSlots()) {
                    ItemStack itemStack = slot.getSecond().copy();
                    PacketItemModifier.callModify(new WrappedItemStack(itemStack), player, PacketItemModifier.Context.EQUIPMENT);
                    items.add(Pair.of(slot.getFirst(), itemStack));
                }

                ClientboundSetEquipmentPacket newEquipmentPacket = new ClientboundSetEquipmentPacket(packet.getEntity(), items);
                super.write(ctx, newEquipmentPacket, promise);
            } else {
                super.write(ctx, msg, promise);
            }
        } else {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (com.artillexstudios.axapi.utils.FeatureFlags.DEBUG.get()) {
            log.error("An unhandled exception occurred on ctx {}!", ctx, cause);
        }

        super.exceptionCaught(ctx, cause);
    }
}
