package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import com.artillexstudios.axapi.utils.RotationType;
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

    private void rotation(RotationType type, EulerAngle eulerAngle) {
        switch (type) {
            case HEAD: {
                this.metadata.set(Accessors.HEAD_ROTATION, eulerAngle);
                break;
            }
            case BODY: {
                this.metadata.set(Accessors.BODY_ROTATION, eulerAngle);
                break;
            }
            case LEFT_ARM: {
                this.metadata.set(Accessors.LEFT_ARM_ROTATION, eulerAngle);
                break;
            }
            case RIGHT_ARM: {
                this.metadata.set(Accessors.RIGHT_ARM_ROTATION, eulerAngle);
                break;
            }
            case LEFT_LEG: {
                this.metadata.set(Accessors.LEFT_LEG_ROTATION, eulerAngle);
                break;
            }
            case RIGHT_LEG: {
                this.metadata.set(Accessors.RIGHT_LEG_ROTATION, eulerAngle);
                break;
            }
        }
    }

    public EulerAngle rotation(RotationType type) {
        switch (type) {
            case HEAD: {
                return this.metadata.get(Accessors.HEAD_ROTATION);
            }
            case BODY: {
                return this.metadata.get(Accessors.BODY_ROTATION);
            }
            case LEFT_ARM: {
                return this.metadata.get(Accessors.LEFT_ARM_ROTATION);
            }
            case RIGHT_ARM: {
                return this.metadata.get(Accessors.RIGHT_ARM_ROTATION);
            }
            case LEFT_LEG: {
                return this.metadata.get(Accessors.LEFT_LEG_ROTATION);
            }
            case RIGHT_LEG: {
                return this.metadata.get(Accessors.RIGHT_LEG_ROTATION);
            }
        }

        return null;
    }

    @Override
    protected void defineDefaults() {
        this.metadata.define(Accessors.ARMOR_STAND_DATA, (byte) 0);
        this.metadata.define(Accessors.HEAD_ROTATION, new EulerAngle(0, 0, 0));
        this.metadata.define(Accessors.BODY_ROTATION, new EulerAngle(0, 0, 0));
        this.metadata.define(Accessors.LEFT_ARM_ROTATION, new EulerAngle(-10, 0, -10));
        this.metadata.define(Accessors.RIGHT_ARM_ROTATION, new EulerAngle(-15, 0, 10));
        this.metadata.define(Accessors.LEFT_LEG_ROTATION, new EulerAngle(-1, 0, -1));
        this.metadata.define(Accessors.RIGHT_LEG_ROTATION, new EulerAngle(-1, 0, 1));
    }
}
