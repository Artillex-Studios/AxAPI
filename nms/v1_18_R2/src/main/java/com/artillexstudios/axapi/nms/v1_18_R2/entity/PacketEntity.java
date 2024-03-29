package com.artillexstudios.axapi.nms.v1_18_R2.entity;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.lang.reflect.Field;
import java.time.Duration;

public class PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketEntity {
    private static AtomicInteger ENTITY_COUNTER;
    private static final Cache<Component, Optional<net.minecraft.network.chat.Component>> CACHE = Caffeine.newBuilder().maximumSize(600).scheduler(Scheduler.systemScheduler()).expireAfterAccess(Duration.ofSeconds(60)).build();
    public final int entityId;
    private final net.minecraft.world.entity.EntityType<?> entityType;
    private final List<Consumer<PacketEntityInteractEvent>> eventConsumers = new ArrayList<>();
    private final NonNullList<net.minecraft.world.item.ItemStack> handSlots;
    private final NonNullList<net.minecraft.world.item.ItemStack> armorSlots;
    public boolean invisible = false;
    public boolean silent = false;
    public com.artillexstudios.axapi.nms.v1_18_R2.entity.SynchedEntityData data;
    public EntityTracker.TrackedEntity tracker;
    private List<SynchedEntityData.DataItem<?>> trackedValues;
    private Location location;
    private Component name = Component.empty();
    private int viewDistance = 32;
    private int viewDistanceSquared = 32 * 32;
    private boolean itemDirty = false;
    private boolean shouldTeleport = false;
    public ServerLevel level;
    private int ridingEntity = 0;
    public Predicate<Player> predicate;

    static {
        try {
            Field entityIdField = Entity.class.getDeclaredField("c");
            entityIdField.setAccessible(true);
            ENTITY_COUNTER = (AtomicInteger) entityIdField.get(null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public PacketEntity(EntityType entityType, Location location, Consumer<com.artillexstudios.axapi.entity.impl.PacketEntity> consumer) {
        entityId = ENTITY_COUNTER.incrementAndGet();
        this.location = location;
        this.level = ((CraftWorld) location.getWorld()).getHandle();
        this.entityType = net.minecraft.world.entity.EntityType.byString(entityType.getName()).orElse(net.minecraft.world.entity.EntityType.ARMOR_STAND);
        data = new com.artillexstudios.axapi.nms.v1_18_R2.entity.SynchedEntityData();
        defineEntityData();
        trackedValues = data.getAll();

        handSlots = NonNullList.withSize(2, net.minecraft.world.item.ItemStack.EMPTY);
        armorSlots = NonNullList.withSize(4, net.minecraft.world.item.ItemStack.EMPTY);

        if (consumer != null) {
            consumer.accept(this);
        }

        AxPlugin.tracker.addEntity(this);
    }

    private static net.minecraft.world.item.ItemStack stripMeta(net.minecraft.world.item.ItemStack itemStack, boolean copyItemStack) {
        if (!itemStack.isEmpty() && (itemStack.hasTag() || itemStack.getCount() >= 2)) {
            net.minecraft.world.item.ItemStack copy = copyItemStack ? itemStack.copy() : itemStack;

            CompoundTag tag = copy.getTag();
            copy.setCount(copy.getCount() > 1 ? 2 : 1);
            if (tag != null) {
                Tag var6 = tag.get("display");
                if (var6 instanceof CompoundTag displayTag) {
                    displayTag.remove("Lore");
                    displayTag.remove("Name");
                }

                var6 = tag.get("Enchantments");
                if (var6 instanceof ListTag enchantmentsTag) {
                    if (!enchantmentsTag.isEmpty()) {
                        ListTag enchantments = new ListTag();
                        CompoundTag fakeEnchantment = new CompoundTag();
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SOUL_SPEED, itemStack) > 0) {
                            fakeEnchantment.putString("id", Enchantment.SOUL_SPEED.getKey().asString());
                            fakeEnchantment.putInt("lvl", 1);
                        }

                        enchantments.add(fakeEnchantment);
                        tag.put("Enchantments", enchantments);
                    }
                }

                tag.remove("AttributeModifiers");
                tag.remove("author");
                tag.remove("filtered_title");
                tag.remove("pages");
                tag.remove("filtered_pages");
                tag.remove("title");
                tag.remove("generation");
            }


            tag.remove("LodestonePos");
            if (tag.contains("LodestoneDimension")) {
                tag.putString("LodestoneDimension", "paper:paper");
            }

            return copy;
        } else {
            return itemStack;
        }
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
                return Optional.ofNullable(net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(key)));
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

    }

    @Override
    public void hide(Player player) {

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
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(entityId);
        buf.writeInt(entity.getEntityId());
        this.tracker.broadcast(new ClientboundSetEntityLinkPacket(buf));
        buf.release();
    }

    @Override
    public void ride(com.artillexstudios.axapi.entity.impl.PacketEntity entity) {
        ridingEntity = entity.getEntityId();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(entityId);
        buf.writeInt(entity.getEntityId());
        this.tracker.broadcast(new ClientboundSetEntityLinkPacket(buf));
        buf.release();
    }

    @Override
    public void unRide() {
        ridingEntity = 0;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(entityId);
        buf.writeInt(0);
        this.tracker.broadcast(new ClientboundSetEntityLinkPacket(buf));
        buf.release();
    }

    @Override
    public void sendMetaUpdate() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(entityId);
        SynchedEntityData.pack(data.packForNameUpdate(), buf);

        ClientboundSetEntityDataPacket clientboundSetEntityDataPacket = new ClientboundSetEntityDataPacket(buf);
        buf.release();
        this.tracker.broadcast(clientboundSetEntityDataPacket);
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
        };
    }

    public void addPairing(ServerPlayer player) {
        addPairingData(player.connection::send);
    }

    private void addPairingData(Consumer<Packet<ClientGamePacketListener>> consumer) {
        var packet = getAddEntityPacket();
        consumer.accept(packet);

        if (trackedValues != null) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            SynchedEntityData.pack(trackedValues, buf);

            consumer.accept(new ClientboundSetEntityDataPacket(buf));
            buf.release();
        }

        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipments = Lists.newArrayList();
        for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
            var item = getItemBySlot(slot);

            if (!item.isEmpty()) {
                var sanitised = LivingEntity.sanitizeItemStack(item.copy(), false);
                equipments.add(Pair.of(slot, stripMeta(sanitised, false)));
            } else {
                equipments.add(Pair.of(slot, item));
            }
        }

        if (ridingEntity != 0) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(entityId);
            buf.writeInt(0);
            consumer.accept(new ClientboundSetEntityLinkPacket(buf));
            buf.release();
        }

        if (!equipments.isEmpty()) {
            consumer.accept(new ClientboundSetEquipmentPacket(entityId, equipments));
        }
    }

    public void removePairing(ServerPlayer player) {
        player.connection.send(new ClientboundRemoveEntitiesPacket(entityId));
    }

    private ClientboundAddEntityPacket getAddEntityPacket() {
        return new ClientboundAddEntityPacket(entityId, UUID.randomUUID(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), this.entityType, 1, Vec3.ZERO);
    }

    public void sendChanges() {
        sendDirtyEntityData();

        if (itemDirty) {
            itemDirty = false;
            List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipments = Lists.newArrayList();
            net.minecraft.world.entity.EquipmentSlot[] equipmentSlots = net.minecraft.world.entity.EquipmentSlot.values();
            var i = equipmentSlots.length;

            for (int j = 0; j < i; j++) {
                var slot = equipmentSlots[j];
                var item = getItemBySlot(slot);

                if (!item.isEmpty()) {
                    var sanitised = LivingEntity.sanitizeItemStack(item.copy(), false);
                    equipments.add(Pair.of(slot, stripMeta(sanitised, false)));
                }
            }

            if (!equipments.isEmpty()) {
                tracker.broadcast(new ClientboundSetEquipmentPacket(entityId, equipments));
            }
        }

        if (shouldTeleport) {
            shouldTeleport = false;

            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entityId);
            byteBuf.writeDouble(this.location.getX());
            byteBuf.writeDouble(this.location.getY());
            byteBuf.writeDouble(this.location.getZ());
            byteBuf.writeByte((byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
            byteBuf.writeByte((byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
            byteBuf.writeBoolean(true);

            tracker.broadcast(new ClientboundTeleportEntityPacket(byteBuf));

            byteBuf.release();
        }
    }

    private void sendDirtyEntityData() {
        List<SynchedEntityData.DataItem<?>> list = data.packDirty();

        if (list != null) {
            this.trackedValues = data.getAll();
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            SynchedEntityData.pack(list, buf);

            this.tracker.broadcast(new ClientboundSetEntityDataPacket(buf));
            buf.release();
        }
    }

    public void defineEntityData() {
        data.define(EntityData.BYTE_DATA, (byte) 0);
        data.define(EntityData.CUSTOM_NAME_VISIBLE, false);
        data.define(EntityData.CUSTOM_NAME, Optional.empty());
        data.define(EntityData.SILENT, false);
    }
}
