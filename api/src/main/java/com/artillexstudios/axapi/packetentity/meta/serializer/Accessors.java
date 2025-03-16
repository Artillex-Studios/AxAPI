package com.artillexstudios.axapi.packetentity.meta.serializer;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.utils.ParticleArguments;
import com.artillexstudios.axapi.utils.Quaternion;
import com.artillexstudios.axapi.utils.Vector3f;
import net.kyori.adventure.text.Component;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Pose;
import org.bukkit.util.EulerAngle;

import java.util.Optional;

public final class Accessors {
    public static final EntityDataAccessor<Byte> SHARED_FLAGS = EntityDataSerializers.BYTE.createAccessor(0);
    public static final EntityDataAccessor<Integer> AIR_TICKS = EntityDataSerializers.INT.createAccessor(1);
    public static final EntityDataAccessor<Optional<Component>> CUSTOM_NAME = EntityDataSerializers.OPTIONAL_COMPONENT.createAccessor(2);
    public static final EntityDataAccessor<Boolean> CUSTOM_NAME_VISIBLE = EntityDataSerializers.BOOLEAN.createAccessor(3);
    public static final EntityDataAccessor<Boolean> SILENT = EntityDataSerializers.BOOLEAN.createAccessor(4);
    public static final EntityDataAccessor<Boolean> HAS_NO_GRAVITY = EntityDataSerializers.BOOLEAN.createAccessor(5);
    public static final EntityDataAccessor<Pose> POSE = EntityDataSerializers.POSE.createAccessor(6);
    public static final EntityDataAccessor<Integer> TICKS_FROZEN = EntityDataSerializers.INT.createAccessor(7);
    public static final EntityDataAccessor<Byte> ARMOR_STAND_DATA = EntityDataSerializers.BYTE.createAccessor(15);
    public static final EntityDataAccessor<EulerAngle> HEAD_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(16);
    public static final EntityDataAccessor<EulerAngle> BODY_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(17);
    public static final EntityDataAccessor<EulerAngle> LEFT_ARM_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(18);
    public static final EntityDataAccessor<EulerAngle> RIGHT_ARM_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(19);
    public static final EntityDataAccessor<EulerAngle> LEFT_LEG_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(20);
    public static final EntityDataAccessor<EulerAngle> RIGHT_LEG_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(21);
    public static final EntityDataAccessor<WrappedItemStack> ITEM_SLOT = EntityDataSerializers.ITEM_STACK.createAccessor(8);
    public static final EntityDataAccessor<Float> AREA_EFFECT_CLOUD_RADIUS = EntityDataSerializers.FLOAT.createAccessor(8);
    public static final EntityDataAccessor<Integer> AREA_EFFECT_CLOUD_COLOR = EntityDataSerializers.INT.createAccessor(9);
    public static final EntityDataAccessor<Boolean> AREA_EFFECT_CLOUD_POINT = EntityDataSerializers.BOOLEAN.createAccessor(10);
    public static final EntityDataAccessor<ParticleArguments> AREA_EFFECT_CLOUD_PARTICLE = EntityDataSerializers.PARTICLE.createAccessor(11);
    public static final EntityDataAccessor<Integer> INTERPOLATION_DELAY = EntityDataSerializers.INT.createAccessor(8);
    public static final EntityDataAccessor<Integer> TRANSFORMATION_INTERPOLATION_DURATION = EntityDataSerializers.INT.createAccessor(9);
    public static final EntityDataAccessor<Integer> POSITION_INTERPOLATION_DURATION = EntityDataSerializers.INT.createAccessor(10);
    public static final EntityDataAccessor<Vector3f> TRANSLATION = EntityDataSerializers.VECTOR3.createAccessor(11);
    public static final EntityDataAccessor<Vector3f> SCALE = EntityDataSerializers.VECTOR3.createAccessor(12);
    public static final EntityDataAccessor<Quaternion> ROTATION_LEFT = EntityDataSerializers.QUATERNION.createAccessor(13);
    public static final EntityDataAccessor<Quaternion> ROTATION_RIGHT = EntityDataSerializers.QUATERNION.createAccessor(14);
    public static final EntityDataAccessor<Byte> BILLBOARD_CONSTRAIN = EntityDataSerializers.BYTE.createAccessor(15);
    public static final EntityDataAccessor<Integer> BRIGHTNESS_OVERRIDE = EntityDataSerializers.INT.createAccessor(16);
    public static final EntityDataAccessor<Float> VIEW_RANGE = EntityDataSerializers.FLOAT.createAccessor(17);
    public static final EntityDataAccessor<Float> SHADOW_RADIUS = EntityDataSerializers.FLOAT.createAccessor(18);
    public static final EntityDataAccessor<Float> SHADOW_STRENGTH = EntityDataSerializers.FLOAT.createAccessor(19);
    public static final EntityDataAccessor<Float> WIDTH = EntityDataSerializers.FLOAT.createAccessor(20);
    public static final EntityDataAccessor<Float> HEIGHT = EntityDataSerializers.FLOAT.createAccessor(21);
    public static final EntityDataAccessor<Integer> GLOW_COLOR_OVERRIDE = EntityDataSerializers.INT.createAccessor(22);
    public static final EntityDataAccessor<BlockData> BLOCK_DATA = EntityDataSerializers.BLOCK_DATA.createAccessor(23);
    public static final EntityDataAccessor<WrappedItemStack> DISPLAYED_ITEM = EntityDataSerializers.ITEM_STACK.createAccessor(23);
    public static final EntityDataAccessor<Byte> DISPLAY_TYPE = EntityDataSerializers.BYTE.createAccessor(24);
    public static final EntityDataAccessor<Component> TEXT_COMPONENT = EntityDataSerializers.COMPONENT.createAccessor(23);
    public static final EntityDataAccessor<Integer> LINE_WIDTH = EntityDataSerializers.INT.createAccessor(24);
    public static final EntityDataAccessor<Integer> BACKGROUND_COLOR = EntityDataSerializers.INT.createAccessor(25);
    public static final EntityDataAccessor<Byte> TEXT_OPACITY = EntityDataSerializers.BYTE.createAccessor(26);
    public static final EntityDataAccessor<Byte> TEXT_DISPLAY_DATA = EntityDataSerializers.BYTE.createAccessor(27);
}
