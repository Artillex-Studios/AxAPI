package com.artillexstudios.axapi.nms.v1_18_R1.entity;

import com.artillexstudios.axapi.utils.RotationType;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Rotations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.util.EnumMap;
import java.util.List;

public class PacketArmorStand extends PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketArmorStand {
    private final EnumMap<RotationType, Rotations> rotationOverrides = new EnumMap<>(RotationType.class);
    private boolean small = false;
    private boolean marker = false;
    private boolean hasArms = false;
    private boolean hasBasePlate = true;

    public PacketArmorStand(Location location) {
        super(EntityType.ARMOR_STAND, location);
    }

    private static Rotations toRotations(EulerAngle eulerAngle) {
        return new Rotations((float) Math.toDegrees(eulerAngle.getX()), (float) Math.toDegrees(eulerAngle.getY()), (float) Math.toDegrees(eulerAngle.getZ()));
    }

    @Override
    public void setSmall(boolean small) {
        this.small = small;

        for (Player viewer : viewers) {
            ServerPlayer player = ((CraftPlayer) viewer).getHandle();

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            SynchedEntityData.pack(dataValues(player), buf);

            player.connection.send(new ClientboundSetEntityDataPacket(buf));
            buf.clear();
        }
    }

    @Override
    public void setMarker(boolean marker) {
        this.marker = marker;

        for (Player viewer : viewers) {
            ServerPlayer player = ((CraftPlayer) viewer).getHandle();

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            SynchedEntityData.pack(dataValues(player), buf);

            player.connection.send(new ClientboundSetEntityDataPacket(buf));
            buf.clear();
        }
    }

    @Override
    public void setHasArms(boolean hasArms) {
        this.hasArms = hasArms;

        for (Player viewer : viewers) {
            ServerPlayer player = ((CraftPlayer) viewer).getHandle();

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            SynchedEntityData.pack(dataValues(player), buf);

            player.connection.send(new ClientboundSetEntityDataPacket(buf));
            buf.clear();
        }
    }

    @Override
    public void setHasBasePlate(boolean hasBasePlate) {
        this.hasBasePlate = hasBasePlate;

        for (Player viewer : viewers) {
            ServerPlayer player = ((CraftPlayer) viewer).getHandle();

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            SynchedEntityData.pack(dataValues(player), buf);

            player.connection.send(new ClientboundSetEntityDataPacket(buf));
            buf.clear();
        }
    }

    @Override
    public void setRotation(RotationType rotationType, EulerAngle rotation) {
        rotationOverrides.put(rotationType, toRotations(rotation));

        for (Player viewer : viewers) {
            ServerPlayer player = ((CraftPlayer) viewer).getHandle();

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeVarInt(entityId);
            SynchedEntityData.pack(dataValues(player), buf);

            player.connection.send(new ClientboundSetEntityDataPacket(buf));
            buf.clear();
        }
    }

    @Override
    public List<SynchedEntityData.DataItem<?>> dataValues(ServerPlayer player) {
        List<SynchedEntityData.DataItem<?>> values = super.dataValues(player);

        byte mask = (byte) ((small ? 0x01 : 0) | (hasArms ? 0x04 : 0) | (!hasBasePlate ? 0x08 : 0) | (marker ? 0x10 : 0));

        if (mask != 0) {
            values.add(new SynchedEntityData.DataItem<>(EntityDataSerializers.BYTE.createAccessor(15), mask));
        }

        rotationOverrides.forEach((key, value) -> values.add(new SynchedEntityData.DataItem<>(EntityDataSerializers.ROTATIONS.createAccessor(key.index), value)));

        return values;
    }
}
