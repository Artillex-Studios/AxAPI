package com.artillexstudios.axapi.nms.v1_19_R1.entity;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.EntityMetaFactory;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class PacketEntity implements com.artillexstudios.axapi.packetentity.PacketEntity {
    private static final FastFieldAccessor nmsStack = FastFieldAccessor.forClassField(com.artillexstudios.axapi.nms.v1_19_R1.items.WrappedItemStack.class, "itemStack");
    private final int id;
    private final EntityMeta meta;
    private final net.minecraft.world.entity.EntityType<?> type;
    private final VecDeltaCodec codec = new VecDeltaCodec();
    private final Set<Player> invertedVisibilityEntities = Collections.newSetFromMap(new WeakHashMap<>());
    private final NonNullList<ItemStack> handSlots;
    private final NonNullList<ItemStack> armorSlots;
    private Location location;
    private EntityTracker.TrackedEntity tracker;
    private List<SynchedEntityData.DataItem<?>> trackedValues;
    private Vec3 vec3;
    private volatile boolean shouldTeleport = false;
    private volatile boolean itemDirty = false;
    private boolean visibleByDefault = true;
    private Consumer<PacketEntityInteractEvent> interactConsumer;

    public PacketEntity(EntityType entityType, Location location) {
        this.id = NMSHandlers.getNmsHandler().nextEntityId();
        this.type = net.minecraft.world.entity.EntityType.byString(entityType.getName()).orElse(net.minecraft.world.entity.EntityType.ARMOR_STAND);
        this.meta = EntityMetaFactory.getForType(entityType);
        this.location = location;
        this.handSlots = NonNullList.withSize(2, ItemStack.EMPTY);
        this.armorSlots = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    private static List<SynchedEntityData.DataItem<?>> trackedValues(EntityMeta meta) {
        List<SynchedEntityData.DataItem<?>> dataValues = null;
        List<Metadata.DataItem<?>> nonDefaultValues = meta.metadata().getNonDefaultValues();

        if (nonDefaultValues != null) {
            dataValues = new ArrayList<>(nonDefaultValues.size());

            for (Metadata.DataItem<?> dataItem : nonDefaultValues) {
                Serializers.Transformer<?> transformer = Serializers.transformer(dataItem.getAccessor());

                dataValues.add(new SynchedEntityData.DataItem<>((EntityDataAccessor<Object>) transformer.serializer().createAccessor(dataItem.getAccessor().id()), transformer.transform(dataItem.getValue())));
            }
        }

        return dataValues;
    }

    private static List<SynchedEntityData.DataItem<?>> dirtyValues(EntityMeta meta) {
        List<SynchedEntityData.DataItem<?>> dataValues = null;
        List<Metadata.DataItem<?>> dirty = meta.metadata().packDirty();

        if (dirty != null) {
            dataValues = new ArrayList<>(dirty.size());

            for (Metadata.DataItem<?> dataItem : dirty) {
                Serializers.Transformer<?> transformer = Serializers.transformer(dataItem.getAccessor());

                dataValues.add(new SynchedEntityData.DataItem<>((EntityDataAccessor<Object>) transformer.serializer().createAccessor(dataItem.getAccessor().id()), transformer.transform(dataItem.getValue())));
            }
        }

        return dataValues;
    }

    private static ClientboundAddEntityPacket getAddEntityPacket(PacketEntity entity) {
        return new ClientboundAddEntityPacket(entity.id(), UUID.randomUUID(), entity.location().getX(), entity.location().getY(), entity.location().getZ(), entity.location().getPitch(), entity.location().getYaw(), entity.type, 1, Vec3.ZERO, 0);
    }

    @Override
    public void teleport(Location location) {
        this.location = location;
        this.vec3 = new Vec3(location.getX(), location.getY(), location.getZ());
        this.shouldTeleport = true;
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
    public void spawn() {
        this.trackedValues = trackedValues(this.meta);

        AxPlugin.tracker.addEntity(this);
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
    public void show(Player player) {
        if (this.visibleByDefault) {
            this.invertedVisibilityEntities.remove(player);
        } else {
            this.invertedVisibilityEntities.add(player);
        }
    }

    @Override
    public void setVisibleByDefault(boolean visible) {
        this.visibleByDefault = visible;
    }

    @Override
    public void setItem(EquipmentSlot slot, WrappedItemStack item) {
        if (slot.getType() == EquipmentSlot.Type.HAND) {
            this.handSlots.set(slot.getIndex(), nmsStack.get(item));
        } else {
            this.armorSlots.set(slot.getIndex(), nmsStack.get(item));
        }

        this.itemDirty = true;
    }

    @Override
    public WrappedItemStack getItem(EquipmentSlot slot) {
        return new com.artillexstudios.axapi.nms.v1_19_R1.items.WrappedItemStack(slot.getType() == EquipmentSlot.Type.ARMOR ? this.armorSlots.get(slot.getIndex()) : this.handSlots.get(slot.getIndex()));
    }

    @Override
    public void sendChanges() {
        if (this.meta.metadata().isDirty()) {
            List<SynchedEntityData.DataItem<?>> dirty = dirtyValues(this.meta);

            if (dirty != null) {
                this.trackedValues = trackedValues(this.meta);
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeVarInt(this.id);
                SynchedEntityData.pack(dirty, buf);

                this.tracker.broadcast(new ClientboundSetEntityDataPacket(buf));
                buf.release();
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
            this.shouldTeleport = false;
            long k = this.codec.encodeX(vec3);
            long l = this.codec.encodeY(vec3);
            long i1 = this.codec.encodeZ(vec3);
            boolean flag6 = k < -32768L || k > 32767L || l < -32768L || l > 32767L || i1 < -32768L || i1 > 32767L;
            this.codec.setBase(vec3);

            if (!flag6) {
                this.tracker.broadcast(new ClientboundMoveEntityPacket.Pos(this.id, (short) ((int) k), (short) ((int) l), (short) ((int) i1), true));
            } else {
                FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
                byteBuf.writeVarInt(this.id);
                byteBuf.writeDouble(this.location.getX());
                byteBuf.writeDouble(this.location.getY());
                byteBuf.writeDouble(this.location.getZ());
                byteBuf.writeByte((byte) ((int) (this.location.getYaw() * 256.0F / 360.0F)));
                byteBuf.writeByte((byte) ((int) (this.location.getPitch() * 256.0F / 360.0F)));
                byteBuf.writeBoolean(true);

                this.tracker.broadcast(new ClientboundTeleportEntityPacket(byteBuf));
                byteBuf.release();
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

        serverPlayer.connection.send(getAddEntityPacket(this));

        if (this.trackedValues != null) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(this.id);
            SynchedEntityData.pack(this.trackedValues, buf);

            serverPlayer.connection.send(new ClientboundSetEntityDataPacket(buf));
            buf.release();
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
            serverPlayer.connection.send(new ClientboundSetEquipmentPacket(this.id, equipments));
        }
    }

    // TODO: Reimplement predicates here
    @Override
    public boolean canSee(Player player) {
        return this.visibleByDefault ^ this.invertedVisibilityEntities.contains(player);
    }

    @Override
    public void remove() {
        AxPlugin.tracker.removeEntity(this);
        this.location = null;
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

    private ItemStack getItemBySlot(net.minecraft.world.entity.EquipmentSlot slot) {
        return switch (slot.getType()) {
            case ARMOR -> this.armorSlots.get(slot.getIndex());
            case HAND -> this.handSlots.get(slot.getIndex());
        };
    }
}
