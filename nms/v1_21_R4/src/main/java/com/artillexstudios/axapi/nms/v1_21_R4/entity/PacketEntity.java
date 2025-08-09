package com.artillexstudios.axapi.nms.v1_21_R4.entity;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.collections.RawReferenceOpenHashSet;
import com.artillexstudios.axapi.collections.ThreadSafeList;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.hologram.old.HologramLine;
import com.artillexstudios.axapi.hologram.old.Holograms;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.v1_21_R4.packet.ClientboundSetPassengersWrapper;
import com.artillexstudios.axapi.nms.v1_21_R4.packet.ClientboundTeleportEntityWrapper;
import com.artillexstudios.axapi.nms.v1_21_R4.wrapper.ServerWrapper;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.EntityMetaFactory;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import com.artillexstudios.axapi.utils.placeholder.StaticPlaceholder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class PacketEntity implements com.artillexstudios.axapi.packetentity.PacketEntity {
    private final int id;
    private final EntityMeta meta;
    private final net.minecraft.world.entity.EntityType<?> type;
    private final VecDeltaCodec codec = new VecDeltaCodec();
    private final Set<Player> invertedVisibilityEntities = Collections.newSetFromMap(new WeakHashMap<>());
    private final NonNullList<ItemStack> handSlots;
    private final NonNullList<ItemStack> armorSlots;
    private Location location;
    private volatile EntityTracker.TrackedEntity tracker;
    private List<SynchedEntityData.DataValue<?>> trackedValues;
    private Vec3 vec3;
    private volatile boolean shouldTeleport = false;
    private volatile boolean itemDirty = false;
    private boolean visibleByDefault = true;
    private int riddenEntityId = -1;
    private Consumer<PacketEntityInteractEvent> interactConsumer;
    private boolean hasInvertedVisibility = false;
    private float yHeadRot = 0;
    private int viewDistanceSquared = 32 * 32;

    public PacketEntity(EntityType entityType, Location location) {
        this.id = ServerWrapper.INSTANCE.nextEntityId();
        this.type = net.minecraft.world.entity.EntityType.byString(entityType.getName()).orElse(net.minecraft.world.entity.EntityType.ARMOR_STAND);
        this.meta = EntityMetaFactory.getForType(entityType);
        this.location = location;
        this.handSlots = NonNullList.withSize(2, ItemStack.EMPTY);
        this.armorSlots = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    private static List<SynchedEntityData.DataValue<?>> transform(List<Metadata.DataItem<?>> toTransform) {
        List<SynchedEntityData.DataValue<?>> dataValues = null;

        if (toTransform != null) {
            dataValues = new ArrayList<>(toTransform.size());

            for (Metadata.DataItem<?> dataItem : toTransform) {
                Serializers.Transformer<?> transformer = Serializers.transformer(dataItem.serializer());

                dataValues.add(new SynchedEntityData.DataValue<>(dataItem.id(), (EntityDataSerializer<Object>) transformer.serializer(), transformer.transform(dataItem.getValue())));
            }
        }

        return dataValues;
    }

    private static ClientboundAddEntityPacket getAddEntityPacket(PacketEntity entity) {
        return new ClientboundAddEntityPacket(entity.id(), UUID.randomUUID(), entity.location().getX(), entity.location().getY(), entity.location().getZ(), entity.location().getPitch(), entity.location().getYaw(), entity.type, 0, Vec3.ZERO, 0);
    }

    @Override
    public void teleport(Location location) {
        Preconditions.checkNotNull(location, "Can't teleport a packetentity to a null location!");
        this.location = location;
        synchronized (this.codec) {
            this.vec3 = new Vec3(location.getX(), location.getY(), location.getZ());
            this.shouldTeleport = true;
        }
    }

    @Override
    public Location location() {
        return this.location;
    }

    @Override
    public EntityMeta meta() {
        return this.meta;
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public int viewDistanceSquared() {
        return this.viewDistanceSquared;
    }

    @Override
    public void viewDistance(int blocks) {
        this.viewDistanceSquared = blocks * blocks;
    }

    @Override
    public void spawn() {
        this.meta.metadata().markNotDirty();
        this.trackedValues = transform(this.meta.metadata().getNonDefaultValues());

        AxPlugin.getPlugin(AxPlugin.class).tracker.addEntity(this);
    }

    @Override
    public void hide(Player player) {
        if (this.visibleByDefault) {
            this.invertedVisibilityEntities.add(player);
            this.hasInvertedVisibility = true;
        } else {
            this.invertedVisibilityEntities.remove(player);

            if (this.invertedVisibilityEntities.isEmpty()) {
                this.hasInvertedVisibility = false;
            }
        }
    }

    @Override
    public void show(Player player) {
        if (this.visibleByDefault) {
            this.invertedVisibilityEntities.remove(player);

            if (this.invertedVisibilityEntities.isEmpty()) {
                this.hasInvertedVisibility = false;
            }
        } else {
            this.invertedVisibilityEntities.add(player);
            this.hasInvertedVisibility = true;
        }
    }

    @Override
    public void setVisibleByDefault(boolean visible) {
        this.visibleByDefault = visible;
    }

    @Override
    public void setItem(EquipmentSlot slot, WrappedItemStack item) {
        if (slot.getType() == EquipmentSlot.Type.HAND) {
            this.handSlots.set(slot.getIndex(), item == null ? ItemStack.EMPTY : ((com.artillexstudios.axapi.nms.v1_21_R4.items.WrappedItemStack) item).itemStack);
        } else {
            this.armorSlots.set(slot.getIndex(), item == null ? ItemStack.EMPTY : ((com.artillexstudios.axapi.nms.v1_21_R4.items.WrappedItemStack) item).itemStack);
        }

        this.itemDirty = true;
    }

    @Override
    public WrappedItemStack getItem(EquipmentSlot slot) {
        return new com.artillexstudios.axapi.nms.v1_21_R4.items.WrappedItemStack(slot.getType() == EquipmentSlot.Type.ARMOR ? this.armorSlots.get(slot.getIndex()) : this.handSlots.get(slot.getIndex()));
    }

    @Override
    public void sendChanges() {
        List<Metadata.DataItem<?>> items = this.meta.metadata().packDirty();
        if (items != null) {
            List<SynchedEntityData.DataValue<?>> dirty = transform(items);
            this.trackedValues = transform(this.meta.metadata().getNonDefaultValues());

            HologramLine line = Holograms.byId(this.id);
            if (line == null || !line.hasPlaceholders()) {
                this.tracker.broadcast(new ClientboundSetEntityDataPacket(this.id, dirty));
            } else {
                for (Object player : RawReferenceOpenHashSet.rawSet(this.tracker.seenBy)) {
                    if (player == null) {
                        continue;
                    }

                    ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
                    wrapper.sendPacket(new ClientboundSetEntityDataPacket(this.id, this.translate(wrapper.wrapped(), line, dirty)));
                }
            }
        }

        if (this.itemDirty) {
            this.itemDirty = false;
            List<Pair<net.minecraft.world.entity.EquipmentSlot, ItemStack>> equipments = Lists.newArrayList();
            for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
                ItemStack item = getItemBySlot(slot);

                if (item != null && !item.isEmpty()) {
                    ItemStack sanitised = item.copy();
                    equipments.add(Pair.of(slot, sanitised));
                } else {
                    equipments.add(Pair.of(slot, ItemStack.EMPTY));
                }
            }

            if (!equipments.isEmpty()) {
                this.tracker.broadcast(new ClientboundSetEquipmentPacket(this.id, equipments));
            }
        }

        if (this.shouldTeleport) {
            synchronized (this.codec) {
                this.shouldTeleport = false;
                long k = this.codec.encodeX(vec3);
                long l = this.codec.encodeY(vec3);
                long i1 = this.codec.encodeZ(vec3);
                boolean flag6 = k < -32768L || k > 32767L || l < -32768L || l > 32767L || i1 < -32768L || i1 > 32767L;
                this.codec.setBase(vec3);

                if (!flag6) {
                    this.tracker.broadcast(new ClientboundMoveEntityPacket.PosRot(this.id, (short) ((int) k), (short) ((int) l), (short) ((int) i1), (byte) ((int) (location.getYaw() * 256.0F / 360.0F)), (byte) ((int) (location.getPitch() * 256.0F / 360.0F)), true));
                } else {
                    this.tracker.broadcast(ClientboundTeleportEntityWrapper.createNew(this, this.location));
                }
            }
        }
    }

    @Override
    public void removePairing(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(new ClientboundRemoveEntitiesPacket(this.id));
    }

    @Override
    public void addPairing(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ArrayList<Packet<? super ClientGamePacketListener>> list = new ArrayList<>();

        list.add(getAddEntityPacket(this));

        if (this.trackedValues != null) {
            HologramLine line = Holograms.byId(this.id);
            if (line == null || (line.type() != HologramLine.Type.TEXT || !line.hasPlaceholders())) {
                list.add(new ClientboundSetEntityDataPacket(this.id, this.trackedValues));
            } else {
                list.add(new ClientboundSetEntityDataPacket(this.id, this.translate(player, line, this.trackedValues)));
            }
        }

        List<Pair<net.minecraft.world.entity.EquipmentSlot, ItemStack>> equipments = Lists.newArrayList();
        for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
            var item = getItemBySlot(slot);

            if (item != null && !item.isEmpty()) {
                var sanitised = item.copy();
                equipments.add(Pair.of(slot, sanitised));
            }
        }

        if (!equipments.isEmpty()) {
            list.add(new ClientboundSetEquipmentPacket(this.id, equipments));
        }

        if (this.riddenEntityId != -1) {
            list.add(ClientboundSetPassengersWrapper.createNew(this.riddenEntityId, new int[]{this.id}));
        }

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(this.id);
        buf.writeByte((byte) Mth.floor(this.yHeadRot * 256.0F / 360.0F));
        list.add(ClientboundRotateHeadPacket.STREAM_CODEC.decode(buf));
        buf.release();

        serverPlayer.connection.send(new ClientboundBundlePacket(list));
    }

    // TODO: Reimplement predicates here
    @Override
    public boolean canSee(Player player) {
        if (!this.hasInvertedVisibility) {
            return this.visibleByDefault;
        }

        return this.visibleByDefault ^ this.invertedVisibilityEntities.contains(player);
    }

    @Override
    public void remove() {
        AxPlugin.getPlugin(AxPlugin.class).tracker.removeEntity(this);
    }

    @Override
    public void onInteract(Consumer<PacketEntityInteractEvent> event) {
        this.interactConsumer = event;
    }

    @Override
    public void callInteract(PacketEntityInteractEvent event) {
        if (this.interactConsumer != null) {
            this.interactConsumer.accept(event);
        }
    }

    @Override
    public void ride(int entityId) {
        this.riddenEntityId = entityId;
        this.tracker.broadcast(ClientboundSetPassengersWrapper.createNew(this.riddenEntityId, new int[]{this.id}));
    }

    @Override
    public void unRide(int entityId) {
        if (this.riddenEntityId == -1) {
            return;
        }

        this.tracker.broadcast(ClientboundSetPassengersWrapper.createNew(this.riddenEntityId, new int[0]));
        this.riddenEntityId = -1;
    }

    @Override
    public int riddenEntity() {
        return this.riddenEntityId;
    }

    @Override
    public void rotate(float yaw, float pitch) {
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);

        this.tracker.broadcast(new ClientboundMoveEntityPacket.Rot(this.id, (byte) Mth.floor(yaw * 256.0F / 360.0F), (byte) Mth.floor(pitch * 256.0F / 360.0F), true));
    }

    @Override
    public void rotateHead(float yaw) {
        this.yHeadRot = yaw;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(this.id);
        buf.writeByte((byte) Mth.floor(yaw * 256.0F / 360.0F));
        ClientboundRotateHeadPacket packet = ClientboundRotateHeadPacket.STREAM_CODEC.decode(buf);
        this.tracker.broadcast(packet);
        buf.release();
    }

    @Override
    public void update() {
        List<SynchedEntityData.DataValue<?>> transformed = transform(this.meta.metadata().packForNameUpdate());

        HologramLine line = Holograms.byId(this.id);
        if (line == null) {
            return;
        }

        for (Object player : RawReferenceOpenHashSet.rawSet(this.tracker.seenBy)) {
            if (player == null) {
                continue;
            }

            ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
            wrapper.sendPacket(new ClientboundSetEntityDataPacket(this.id, this.translate(wrapper.wrapped(), line, transformed)));
        }
    }

    private List<SynchedEntityData.DataValue<?>> translate(Player player, HologramLine line, List<SynchedEntityData.DataValue<?>> values) {
        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>(values);
        ListIterator<SynchedEntityData.DataValue<?>> iterator = dataValues.listIterator();
        while (iterator.hasNext()) {
            SynchedEntityData.DataValue<?> value = iterator.next();

            if (value.id() == 2 && line.type() == HologramLine.Type.TEXT) {
                Optional<Component> content = (Optional<Component>) value.value();
                if (content.isEmpty()) {
                    return values;
                }

                String legacy = legacyCache.get(content.get(), (minecraftComponent) -> {
                    String gsonText = Component.Serializer.toJson((Component) minecraftComponent, MinecraftServer.getServer().registryAccess());
                    net.kyori.adventure.text.Component gsonComponent = GsonComponentSerializer.gson().deserialize(gsonText);
                    return LEGACY_COMPONENT_SERIALIZER.serialize(gsonComponent);
                });

                if (legacy == null) {
                    return values;
                }

                ThreadSafeList<Placeholder> placeholders = line.placeholders();
                for (int i = 0; i < placeholders.size(); i++) {
                    Placeholder placeholder = placeholders.get(i);
                    if (placeholder instanceof StaticPlaceholder) continue;
                    legacy = placeholder.parse(player, legacy);
                }

                Component component = (Component) componentCache.get(legacy, (legacyText) -> {
                    net.kyori.adventure.text.Component formatted = StringUtils.format(legacyText);
                    String gson = GsonComponentSerializer.gson().serialize(formatted);
                    return Component.Serializer.fromJson(gson, MinecraftServer.getServer().registryAccess());
                });

                iterator.remove();
                iterator.add(new SynchedEntityData.DataValue<>(value.id(), (EntityDataSerializer<Object>) value.serializer(), Optional.ofNullable(component)));
                break;
            }
        }

        return dataValues;
    }

    private ItemStack getItemBySlot(net.minecraft.world.entity.EquipmentSlot slot) {
        return switch (slot.getType()) {
            case HUMANOID_ARMOR -> this.armorSlots.get(slot.getIndex());
            case HAND -> this.handSlots.get(slot.getIndex());
            case ANIMAL_ARMOR, SADDLE -> null;
        };
    }
}
