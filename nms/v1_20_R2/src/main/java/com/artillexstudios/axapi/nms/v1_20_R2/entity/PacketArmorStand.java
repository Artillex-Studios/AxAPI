package com.artillexstudios.axapi.nms.v1_20_R2.entity;

import com.artillexstudios.axapi.utils.RotationType;
import net.minecraft.core.Rotations;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.EulerAngle;

import java.util.function.Consumer;

public class PacketArmorStand extends PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketArmorStand {
    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);
    private boolean small = false;
    private boolean marker = false;
    private boolean hasArms = false;
    private boolean hasBasePlate = true;

    public PacketArmorStand(Location location, Consumer<com.artillexstudios.axapi.entity.impl.PacketEntity> consumer) {
        super(EntityType.ARMOR_STAND, location, consumer);
    }

    private static Rotations toRotations(EulerAngle eulerAngle) {
        return new Rotations((float) Math.toDegrees(eulerAngle.getX()), (float) Math.toDegrees(eulerAngle.getY()), (float) Math.toDegrees(eulerAngle.getZ()));
    }

    @Override
    public void setSmall(boolean small) {
        this.small = small;

        data.set(EntityData.ARMOR_STAND_DATA, (byte) ((small ? 0x01 : 0) | (hasArms ? 0x04 : 0) | (!hasBasePlate ? 0x08 : 0) | (marker ? 0x10 : 0)));
    }

    @Override
    public void setMarker(boolean marker) {
        this.marker = marker;

        data.set(EntityData.ARMOR_STAND_DATA, (byte) ((small ? 0x01 : 0) | (hasArms ? 0x04 : 0) | (!hasBasePlate ? 0x08 : 0) | (marker ? 0x10 : 0)));
    }

    @Override
    public void setHasArms(boolean hasArms) {
        this.hasArms = hasArms;

        data.set(EntityData.ARMOR_STAND_DATA, (byte) ((small ? 0x01 : 0) | (hasArms ? 0x04 : 0) | (!hasBasePlate ? 0x08 : 0) | (marker ? 0x10 : 0)));
    }

    @Override
    public void setHasBasePlate(boolean hasBasePlate) {
        this.hasBasePlate = hasBasePlate;

        data.set(EntityData.ARMOR_STAND_DATA, (byte) ((small ? 0x01 : 0) | (hasArms ? 0x04 : 0) | (!hasBasePlate ? 0x08 : 0) | (marker ? 0x10 : 0)));
    }

    @Override
    public void setRotation(RotationType rotationType, EulerAngle rotation) {
        switch (rotationType) {
            case RIGHT_LEG -> data.set(EntityData.ARMOR_STAND_RIGHT_LEG_ROTATION, toRotations(rotation));
            case RIGHT_ARM -> data.set(EntityData.ARMOR_STAND_RIGHT_ARM_ROTATION, toRotations(rotation));
            case HEAD -> data.set(EntityData.ARMOR_STAND_HEAD_ROTATION, toRotations(rotation));
            case LEFT_ARM -> data.set(EntityData.ARMOR_STAND_LEFT_ARM_ROTATION, toRotations(rotation));
            case LEFT_LEG -> data.set(EntityData.ARMOR_STAND_LEFT_LEG_ROTATION, toRotations(rotation));
            case BODY -> data.set(EntityData.ARMOR_STAND_BODY_ROTATION, toRotations(rotation));
        }
    }

    @Override
    public void defineEntityData() {
        super.defineEntityData();

        data.define(EntityData.ARMOR_STAND_DATA, (byte) 0);
        data.define(EntityData.ARMOR_STAND_HEAD_ROTATION, DEFAULT_HEAD_POSE);
        data.define(EntityData.ARMOR_STAND_BODY_ROTATION, DEFAULT_BODY_POSE);
        data.define(EntityData.ARMOR_STAND_LEFT_ARM_ROTATION, DEFAULT_LEFT_ARM_POSE);
        data.define(EntityData.ARMOR_STAND_RIGHT_ARM_ROTATION, DEFAULT_RIGHT_ARM_POSE);
        data.define(EntityData.ARMOR_STAND_LEFT_LEG_ROTATION, DEFAULT_LEFT_LEG_POSE);
        data.define(EntityData.ARMOR_STAND_RIGHT_LEG_ROTATION, DEFAULT_RIGHT_LEG_POSE);
    }
}
