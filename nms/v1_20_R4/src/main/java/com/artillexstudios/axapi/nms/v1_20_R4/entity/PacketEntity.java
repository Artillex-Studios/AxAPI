package com.artillexstudios.axapi.nms.v1_20_R4.entity;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.nms.v1_20_R4.packet.ClientboundSetPassengersWrapper;
import com.artillexstudios.axapi.nms.v1_20_R4.packet.ClientboundTeleportEntityWrapper;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.kyori.adventure.text.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketEntity {
    private static final Logger log = LoggerFactory.getLogger(PacketEntity.class);
    private static final Cache<Component, Optional<net.minecraft.network.chat.Component>> CACHE = Caffeine.newBuilder().maximumSize(600).scheduler(Scheduler.systemScheduler()).expireAfterAccess(Duration.ofSeconds(60)).build();
    private static AtomicInteger ENTITY_COUNTER;

    static {
        try {
            Field entityIdField = Entity.class.getDeclaredField("c");
            entityIdField.setAccessible(true);
            ENTITY_COUNTER = (AtomicInteger) entityIdField.get(null);
        } catch (Exception exception) {
            log.error("An exception occurred while initializing PacketEntity!", exception);
        }
    }

    public final int entityId;
    private final net.minecraft.world.entity.EntityType<?> entityType;
    private final List<Consumer<PacketEntityInteractEvent>> eventConsumers = new ArrayList<>();
    private final NonNullList<net.minecraft.world.item.ItemStack> handSlots;
    private final NonNullList<net.minecraft.world.item.ItemStack> armorSlots;
    private final Set<Player> invertedVisibilityEntities = Collections.newSetFromMap(new WeakHashMap<>());
    public boolean invisible = false;
    public boolean silent = false;
    public com.artillexstudios.axapi.nms.v1_20_R4.entity.SynchedEntityData data;
    public EntityTracker.TrackedEntity tracker;
    public ServerLevel level;
    public Predicate<Player> predicate;
    private boolean visibleByDefault = true;
    private List<SynchedEntityData.DataValue<?>> trackedValues;
    private Location location;
    private Component name = Component.empty();
    private int viewDistance = 32;
    private int viewDistanceSquared = 32 * 32;
    private boolean itemDirty = false;
    private boolean shouldTeleport = false;
    private int ridingEntity = 0;

    public PacketEntity(EntityType entityType, Location location, Consumer<com.artillexstudios.axapi.entity.impl.PacketEntity> consumer) {
        entityId = ENTITY_COUNTER.incrementAndGet();
        this.location = location;
        this.level = ((CraftWorld) location.getWorld()).getHandle();
        this.entityType = net.minecraft.world.entity.EntityType.byString(entityType.getName()).orElse(net.minecraft.world.entity.EntityType.ARMOR_STAND);
        data = new com.artillexstudios.axapi.nms.v1_20_R4.entity.SynchedEntityData();
        defineEntityData();
        trackedValues = data.getNonDefaultValues();

        handSlots = NonNullList.withSize(2, net.minecraft.world.item.ItemStack.EMPTY);
        armorSlots = NonNullList.withSize(4, net.minecraft.world.item.ItemStack.EMPTY);

        if (consumer != null) {
            consumer.accept(this);
        }

        AxPlugin.tracker.addEntity(this);
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public void setName(Component name) {
        if (name == null) {
            this.name = Component.empty();
            data.set(EntityData.CUSTOM_NAME_VISIBLE, false);
            data.set(EntityData.CUSTOM_NAME, Optional.empty());
            return;
        }

        if (!this.name.equals(name)) {
            this.name = name;

            data.set(EntityData.CUSTOM_NAME_VISIBLE, true);
            Optional<net.minecraft.network.chat.Component> component = CACHE.get(name, key -> {
                return Optional.ofNullable(ComponentSerializer.INSTANCE.toVanilla(key));
            });
            data.set(EntityData.CUSTOM_NAME, component);
        }
    }

    @Override
    public void teleport(Location location) {
        this.location = location;
        this.level = ((CraftWorld) location.getWorld()).getHandle();
        this.shouldTeleport = true;
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        data.set(EntityData.BYTE_DATA, invisible ? (byte) 0x20 : (byte) 0);
    }

    @Override
    public void setSilent(boolean silent) {
        this.silent = silent;
        data.set(EntityData.SILENT, silent);
    }

    @Override
    public Location getLocation() {
        return this.location;
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
        if (this.visibleByDefault) {
            this.invertedVisibilityEntities.remove(player);
        } else {
            this.invertedVisibilityEntities.add(player);
        }
    }

    @Override
    public void hide(Player player) {
        if (this.visibleByDefault) {
            this.invertedVisibilityEntities.add(player);
        } else {
            this.invertedVisibilityEntities.remove(player);
        }
    }

    @Override
    public void remove() {
        AxPlugin.tracker.removeEntity(this);
        level = null;
    }

    @Override
    public void setItem(EquipmentSlot slot, @Nullable ItemStack item) {
        var equipmentSlot = net.minecraft.world.entity.EquipmentSlot.byTypeAndIndex(slot.getType() == EquipmentSlot.Type.ARMOR ? net.minecraft.world.entity.EquipmentSlot.Type.ARMOR : net.minecraft.world.entity.EquipmentSlot.Type.HAND, slot.getIndex());

        setItemSlot(equipmentSlot, item == null ? net.minecraft.world.item.ItemStack.EMPTY : item.getType() == Material.AIR ? net.minecraft.world.item.ItemStack.EMPTY : CraftItemStack.asNMSCopy(item));
    }

    @Nullable
    @Override
    public ItemStack getItem(EquipmentSlot slot) {
        var equipmentSlot = net.minecraft.world.entity.EquipmentSlot.byTypeAndIndex(slot.getType() == EquipmentSlot.Type.ARMOR ? net.minecraft.world.entity.EquipmentSlot.Type.ARMOR : net.minecraft.world.entity.EquipmentSlot.Type.HAND, slot.getIndex());

        return CraftItemStack.asCraftMirror(getItemBySlot(equipmentSlot));
    }

    @Override
    public Set<Player> getViewers() {
        return this.tracker.seenBy.stream().map(it -> it.getPlayer().getBukkitEntity()).collect(Collectors.toSet());
    }

    @Override
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public void shouldSee(Predicate<Player> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void onClick(Consumer<PacketEntityInteractEvent> event) {
        eventConsumers.add(event);
    }

    @Override
    public void removeClickListener(Consumer<PacketEntityInteractEvent> eventConsumer) {
        eventConsumers.remove(eventConsumer);
    }

    @Override
    public void ride(org.bukkit.entity.Entity entity) {
        ridingEntity = entity.getEntityId();
        int[] passengers = new int[]{entityId};
        this.tracker.broadcast(ClientboundSetPassengersWrapper.createNew(ridingEntity, passengers));
    }

    @Override
    public void setGravity(boolean gravity) {
        data.set(EntityData.GRAVITY, !gravity);
    }

    @Override
    public void setVisibleByDefault(boolean invisibleByDefault) {
        this.visibleByDefault = invisibleByDefault;
    }

    public boolean canSee(Player player) {
        if (predicate != null && predicate.test(player)) {
            return visibleByDefault ^ invertedVisibilityEntities.contains(player);
        }

        return visibleByDefault ^ invertedVisibilityEntities.contains(player);
    }

    @Override
    public void ride(com.artillexstudios.axapi.entity.impl.PacketEntity entity) {
        ridingEntity = entity.getEntityId();
        int[] passengers = new int[]{entityId};
        this.tracker.broadcast(ClientboundSetPassengersWrapper.createNew(ridingEntity, passengers));
    }

    @Override
    public void unRide() {
        this.tracker.broadcast(ClientboundSetPassengersWrapper.createNew(ridingEntity, new int[0]));
        ridingEntity = 0;
    }

    @Override
    public void sendMetaUpdate() {
        this.tracker.broadcast(new ClientboundSetEntityDataPacket(entityId, data.packForNameUpdate()));
    }

    public void acceptEventConsumers(PacketEntityInteractEvent event) {
        for (Consumer<PacketEntityInteractEvent> eventConsumer : eventConsumers) {
            eventConsumer.accept(event);
        }
    }

    public int getViewDistanceSquared() {
        return viewDistanceSquared;
    }

    public void setItemSlot(net.minecraft.world.entity.EquipmentSlot equipmentSlot, net.minecraft.world.item.ItemStack itemStack) {
        switch (equipmentSlot.getType()) {
            case HAND -> handSlots.set(equipmentSlot.getIndex(), itemStack);
            case ARMOR -> armorSlots.set(equipmentSlot.getIndex(), itemStack);
        }

        itemDirty = true;
    }

    public net.minecraft.world.item.ItemStack getItemBySlot(net.minecraft.world.entity.EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot.getType()) {
            case HAND -> this.handSlots.get(equipmentSlot.getIndex());
            case ARMOR -> this.armorSlots.get(equipmentSlot.getIndex());
            case BODY -> null;
        };
    }

    public void addPairing(ServerPlayer player) {
        List<Packet<? super ClientGamePacketListener>> list = new ArrayList<>();

        addPairingData(list::add);

        player.connection.send(new ClientboundBundlePacket(list));
    }

    private void addPairingData(Consumer<Packet<ClientGamePacketListener>> consumer) {
        var packet = getAddEntityPacket();
        consumer.accept(packet);

        if (trackedValues != null) {
            consumer.accept(new ClientboundSetEntityDataPacket(this.entityId, this.trackedValues));
        }

        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipments = Lists.newArrayList();
        for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
            var item = getItemBySlot(slot);

            if (item != null && !item.isEmpty()) {
                var sanitised = item.copy();
                equipments.add(Pair.of(slot, sanitised));
            }
        }

        if (ridingEntity != 0) {
            int[] passengers = new int[]{entityId};
            consumer.accept(ClientboundSetPassengersWrapper.createNew(ridingEntity, passengers));
        }

        if (!equipments.isEmpty()) {
            consumer.accept(new ClientboundSetEquipmentPacket(entityId, equipments));
        }
    }

    public void removePairing(ServerPlayer player) {
        player.connection.send(new ClientboundRemoveEntitiesPacket(entityId));
    }

    private ClientboundAddEntityPacket getAddEntityPacket() {
        return new ClientboundAddEntityPacket(entityId, UUID.randomUUID(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), this.entityType, 1, Vec3.ZERO, 0);
    }

    public void sendChanges() {
        if (data.isDirty()) {
            sendDirtyEntityData();
        }

        if (itemDirty) {
            itemDirty = false;
            List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipments = Lists.newArrayList();
            net.minecraft.world.entity.EquipmentSlot[] equipmentSlots = net.minecraft.world.entity.EquipmentSlot.values();
            var i = equipmentSlots.length;

            for (int j = 0; j < i; j++) {
                var slot = equipmentSlots[j];
                var item = getItemBySlot(slot);

                if (item != null && !item.isEmpty()) {
                    var sanitised = item.copy();
                    equipments.add(Pair.of(slot, sanitised));
                } else {
                    equipments.add(Pair.of(slot, net.minecraft.world.item.ItemStack.EMPTY));
                }
            }

            if (!equipments.isEmpty()) {
                tracker.broadcast(new ClientboundSetEquipmentPacket(entityId, equipments));
            }
        }

        if (shouldTeleport) {
            shouldTeleport = false;

            tracker.broadcast(ClientboundTeleportEntityWrapper.createNew(this, location));
        }
    }

    private void sendDirtyEntityData() {
        List<SynchedEntityData.DataValue<?>> list = data.packDirty();

        if (list != null) {
            this.trackedValues = data.getNonDefaultValues();
            this.tracker.broadcast(new ClientboundSetEntityDataPacket(entityId, list));
        }
    }

    public void defineEntityData() {
        data.define(EntityData.BYTE_DATA, (byte) 0);
        data.define(EntityData.CUSTOM_NAME_VISIBLE, false);
        data.define(EntityData.CUSTOM_NAME, Optional.empty());
        data.define(EntityData.SILENT, false);
        data.define(EntityData.GRAVITY, false);
    }
}
