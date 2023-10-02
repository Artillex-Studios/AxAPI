package com.artillexstudios.axapi.nms.v1_20_R1.entity;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketEntity {
    public final Set<Player> viewers = new HashSet<>();
    public final int entityId;
    private final net.minecraft.world.entity.EntityType<?> entityType;
    private final HashMap<UUID, Component> nameOverrides = new HashMap<>();
    private final HashMap<String, Object> keys = new HashMap<>();
    private final HashMap<UUID, Location> locationOverrides = new HashMap<>();
    private final List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipments = new ArrayList<>();
    private final List<UUID> forceHidden = new ArrayList<>();
    private final List<Consumer<PacketEntityInteractEvent>> eventConsumers = new ArrayList<>();
    public boolean invisible = false;
    public boolean silent = false;
    private Location location;
    private Component name = null;
    private int viewDistance = 32;
    private int viewDistanceSquared = 32 * 32;

    public PacketEntity(EntityType entityType, Location location) {
        entityId = Entity.nextEntityId();
        this.location = location;
        this.entityType = net.minecraft.world.entity.EntityType.byString(entityType.getName()).orElse(net.minecraft.world.entity.EntityType.ARMOR_STAND);
    }

    private static net.minecraft.world.entity.EquipmentSlot byName(String name) {
        net.minecraft.world.entity.EquipmentSlot[] values = net.minecraft.world.entity.EquipmentSlot.values();

        for (net.minecraft.world.entity.EquipmentSlot value : values) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }

    @Override
    public void setName(Component name, Player player) {
        nameOverrides.put(player.getUniqueId(), name);

        ServerPlayer viewer = ((CraftPlayer) player).getHandle();
        viewer.connection.send(new ClientboundSetEntityDataPacket(entityId, dataValues(viewer)));
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public void setName(Component name) {
        this.name = name;

        for (Player viewer : viewers) {
            if (nameOverrides.containsKey(viewer.getUniqueId())) continue;

            ServerPlayer player = ((CraftPlayer) viewer).getHandle();
            player.connection.send(new ClientboundSetEntityDataPacket(entityId, dataValues(player)));
        }
    }

    @Override
    public Component getName(Player player) {
        return nameOverrides.getOrDefault(player.getUniqueId(), this.name);
    }

    @Override
    public void teleport(Location location) {
        this.location = location;

        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entityId);
        byteBuf.writeDouble(this.location.getX());
        byteBuf.writeDouble(this.location.getY());
        byteBuf.writeDouble(this.location.getZ());
        byteBuf.writeByte(0);
        byteBuf.writeByte(0);
        byteBuf.writeBoolean(true);

        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(byteBuf);

        for (Player viewer : viewers) {
            if (locationOverrides.containsKey(viewer.getUniqueId())) continue;

            ServerPlayer player = ((CraftPlayer) viewer).getHandle();
            player.connection.send(teleportEntityPacket);
        }

        byteBuf.release();
    }

    @Override
    public void teleport(Location location, Player player) {
        this.locationOverrides.put(player.getUniqueId(), location);

        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entityId);
        byteBuf.writeDouble(location.getX());
        byteBuf.writeDouble(location.getY());
        byteBuf.writeDouble(location.getZ());
        byteBuf.writeByte(0);
        byteBuf.writeByte(0);
        byteBuf.writeBoolean(true);

        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(byteBuf);
        ServerPlayer viewer = ((CraftPlayer) player).getHandle();
        viewer.connection.send(teleportEntityPacket);

        byteBuf.release();
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;

        for (Player viewer : viewers) {
            ServerPlayer player = ((CraftPlayer) viewer).getHandle();

            player.connection.send(new ClientboundSetEntityDataPacket(entityId, dataValues(player)));
        }
    }

    @Override
    public void setSilent(boolean silent) {
        this.silent = silent;

        for (Player viewer : viewers) {
            ServerPlayer player = ((CraftPlayer) viewer).getHandle();

            player.connection.send(new ClientboundSetEntityDataPacket(entityId, dataValues(player)));
        }
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Location getLocation(Player player) {
        return this.locationOverrides.getOrDefault(player.getUniqueId(), this.location);
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    @Override
    public void setViewDistance(int blocks) {
        this.viewDistance = blocks;
        this.viewDistanceSquared = this.viewDistance * this.viewDistance;
    }

    @Override
    public void show(Player player) {
        if (forceHidden.contains(player.getUniqueId())) return;

        try {
            if (viewers.add(player)) {
                ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(
                        entityId,
                        UUID.randomUUID(),
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        0,
                        0,
                        this.entityType,
                        1,
                        Vec3.ZERO,
                        0
                );

                ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
                serverPlayer.connection.send(packet);
                serverPlayer.connection.send(new ClientboundSetEntityDataPacket(entityId, dataValues(serverPlayer)));

                if (!equipments.isEmpty()) {
                    serverPlayer.connection.send(changeEquipment());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void hide(Player player) {
        if (viewers.remove(player)) {
            ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(this.entityId);

            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            serverPlayer.connection.send(packet);
        }
    }

    @Override
    public void forceHide(Player player) {
        forceHidden.add(player.getUniqueId());
        hide(player);
    }

    public void forceShow(Player player) {
        forceHidden.remove(player.getUniqueId());
        show(player);
    }

    @Override
    public void remove() {
        PacketEntityTracker.stopTracking(this);

        for (Player viewer : viewers) {
            hide(viewer);
            clear(viewer);
        }

        viewers.clear();
        eventConsumers.clear();
    }

    @Override
    public void setItem(EquipmentSlot slot, @NotNull ItemStack item) {
        net.minecraft.world.entity.EquipmentSlot equipmentSlot = byName(slot.name());

        if (equipmentSlot != null) {
            equipments.add(Pair.of(equipmentSlot, CraftItemStack.asNMSCopy(item)));

            var packet = changeEquipment();
            for (Player viewer : viewers) {
                ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
                serverPlayer.connection.send(packet);
            }
        }
    }

    @Nullable
    @Override
    public ItemStack getItem(EquipmentSlot equipmentSlot) {
        for (Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack> equipment : equipments) {
            if (equipment.getFirst() == byName(equipmentSlot.name())) {
                return equipment.getSecond().getBukkitStack();
            }
        }

        return null;
    }

    @Override
    public void clear(Player player) {
        viewers.remove(player);
        nameOverrides.remove(player.getUniqueId());
        locationOverrides.remove(player.getUniqueId());
        forceHidden.remove(player.getUniqueId());
    }

    @Override
    public Set<Player> getViewers() {
        return this.viewers;
    }

    @Override
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public <T> void write(String key, T value) {
        keys.put(key, value);
    }

    @Override
    public boolean has(String key) {
        return keys.containsKey(key);
    }

    @Override
    public <T> T get(String key) {
        return (T) keys.get(key);
    }

    @Override
    public void onClick(Consumer<PacketEntityInteractEvent> event) {
        eventConsumers.add(event);
    }

    @Override
    public void removeClickListener(Consumer<PacketEntityInteractEvent> eventConsumer) {
        eventConsumers.remove(eventConsumer);
    }

    public void acceptEventConsumers(PacketEntityInteractEvent event) {
        for (Consumer<PacketEntityInteractEvent> eventConsumer : eventConsumers) {
            eventConsumer.accept(event);
        }
    }

    public int getViewDistanceSquared() {
        return viewDistanceSquared;
    }

    public List<SynchedEntityData.DataValue<?>> dataValues(ServerPlayer player) {
        List<SynchedEntityData.DataValue<?>> values = new ArrayList<>();
        if (invisible) {
            values.add(SynchedEntityData.DataValue.create(EntityDataSerializers.BYTE.createAccessor(0), (byte) 0x20));
        }

        if (silent) {
            values.add(SynchedEntityData.DataValue.create(EntityDataSerializers.BOOLEAN.createAccessor(4), true));
        }

        var component = nameOverrides.get(player.getUUID());
        if (component != null) {
            values.add(SynchedEntityData.DataValue.create(EntityDataSerializers.BOOLEAN.createAccessor(3), true));
            values.add(SynchedEntityData.DataValue.create(EntityDataSerializers.OPTIONAL_COMPONENT.createAccessor(2), Optional.of(PaperAdventure.asVanilla(component))));
        } else if (name != null) {
            values.add(SynchedEntityData.DataValue.create(EntityDataSerializers.BOOLEAN.createAccessor(3), true));
            values.add(SynchedEntityData.DataValue.create(EntityDataSerializers.OPTIONAL_COMPONENT.createAccessor(2), Optional.of(net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(name)))));
        }

        return values;
    }

    public ClientboundSetEquipmentPacket changeEquipment() {
        return new ClientboundSetEquipmentPacket(entityId, equipments);
    }
}
