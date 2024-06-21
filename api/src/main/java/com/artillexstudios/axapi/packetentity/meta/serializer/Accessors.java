package com.artillexstudios.axapi.packetentity.meta.serializer;

import com.artillexstudios.axapi.utils.RotationType;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public final class Accessors {
    public static final EntityDataAccessor<Byte> SHARED_FLAGS = EntityDataSerializers.BYTE.createAccessor(0);
    public static final EntityDataAccessor<Integer> AIR_TICKS = EntityDataSerializers.INT.createAccessor(1);
    public static final EntityDataAccessor<Optional<Component>> CUSTOM_NAME = EntityDataSerializers.OPTIONAL_COMPONENT.createAccessor(2);
    public static final EntityDataAccessor<Boolean> CUSTOM_NAME_VISIBLE = EntityDataSerializers.BOOLEAN.createAccessor(3);
    public static final EntityDataAccessor<Boolean> SILENT = EntityDataSerializers.BOOLEAN.createAccessor(4);
    public static final EntityDataAccessor<Boolean> HAS_NO_GRAVITY = EntityDataSerializers.BOOLEAN.createAccessor(5);
    public static final EntityDataAccessor<Integer> TICKS_FROZEN = EntityDataSerializers.INT.createAccessor(7);
    public static final EntityDataAccessor<Byte> ARMOR_STAND_DATA = EntityDataSerializers.BYTE.createAccessor(15);
    public static final EntityDataAccessor<RotationType> HEAD_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(16);
    public static final EntityDataAccessor<RotationType> BODY_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(17);
    public static final EntityDataAccessor<RotationType> LEFT_ARM_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(18);
    public static final EntityDataAccessor<RotationType> RIGHT_ARM_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(19);
    public static final EntityDataAccessor<RotationType> LEFT_LEG_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(20);
    public static final EntityDataAccessor<RotationType> RIGHT_LEG_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(21);
}
