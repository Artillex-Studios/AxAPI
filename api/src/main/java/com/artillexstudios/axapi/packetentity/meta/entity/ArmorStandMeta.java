package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import org.bukkit.util.EulerAngle;

public class ArmorStandMeta extends LivingEntityMeta {

    public ArmorStandMeta(Metadata metadata) {
        super(metadata);
    }

    public void small(boolean small) {
        this.metadata.set(Accessors.ARMOR_STAND_DATA, this.setBit(this.metadata.get(Accessors.ARMOR_STAND_DATA), 1, small));
    }

    public boolean small() {
        return (this.metadata.get(Accessors.ARMOR_STAND_DATA) & 1) != 0;
    }

    public void showArms(boolean showArms) {
        this.metadata.set(Accessors.ARMOR_STAND_DATA, this.setBit(this.metadata.get(Accessors.ARMOR_STAND_DATA), 4, showArms));
    }

    public boolean showArms() {
        return (this.metadata.get(Accessors.ARMOR_STAND_DATA) & 4) != 0;
    }

    public void setNoBasePlate(boolean hideBasePlate) {
        this.metadata.set(Accessors.ARMOR_STAND_DATA, this.setBit(this.metadata.get(Accessors.ARMOR_STAND_DATA), 8, hideBasePlate));
    }

    public boolean noBasePlate() {
        return (this.metadata.get(Accessors.ARMOR_STAND_DATA) & 8) != 0;
    }

    public void marker(boolean marker) {
        this.metadata.set(Accessors.ARMOR_STAND_DATA, this.setBit(this.metadata.get(Accessors.ARMOR_STAND_DATA), 16, marker));
    }

    public boolean marker() {
        return (this.metadata.get(Accessors.ARMOR_STAND_DATA) & 16) != 0;
    }

    private byte setBit(byte value, int bitField, boolean set) {
        if (set) {
            value = (byte)(value | bitField);
        } else {
            value = (byte)(value & ~bitField);
        }

        return value;
    }

    @Override
    protected void defineDefaults() {
        super.defineDefaults();
        this.metadata.define(Accessors.ARMOR_STAND_DATA, (byte) 0);
        this.metadata.define(Accessors.HEAD_ROTATION, new EulerAngle(0, 0, 0));
        this.metadata.define(Accessors.BODY_ROTATION, new EulerAngle(0, 0, 0));
        this.metadata.define(Accessors.LEFT_ARM_ROTATION, new EulerAngle(-10, 0, -10));
        this.metadata.define(Accessors.RIGHT_ARM_ROTATION, new EulerAngle(-15, 0, 10));
        this.metadata.define(Accessors.LEFT_LEG_ROTATION, new EulerAngle(-1, 0, -1));
        this.metadata.define(Accessors.RIGHT_LEG_ROTATION, new EulerAngle(-1, 0, 1));
    }
}
