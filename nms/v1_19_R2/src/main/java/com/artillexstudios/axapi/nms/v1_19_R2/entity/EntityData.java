package com.artillexstudios.axapi.nms.v1_19_R2.entity;

import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class EntityData {
    public static final EntityDataAccessor<Byte> BYTE_DATA = EntityDataSerializers.BYTE.createAccessor(0);
    public static final EntityDataAccessor<Optional<Component>> CUSTOM_NAME = EntityDataSerializers.OPTIONAL_COMPONENT.createAccessor(2);
    public static final EntityDataAccessor<Boolean> CUSTOM_NAME_VISIBLE = EntityDataSerializers.BOOLEAN.createAccessor(3);
    public static final EntityDataAccessor<Boolean> SILENT = EntityDataSerializers.BOOLEAN.createAccessor(4);
    public static final EntityDataAccessor<ItemStack> ITEM_ITEM_STACK = EntityDataSerializers.ITEM_STACK.createAccessor(8);
    public static final EntityDataAccessor<Byte> ARMOR_STAND_DATA = EntityDataSerializers.BYTE.createAccessor(15);
    public static final EntityDataAccessor<Rotations> ARMOR_STAND_HEAD_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(16);
    public static final EntityDataAccessor<Rotations> ARMOR_STAND_BODY_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(17);
    public static final EntityDataAccessor<Rotations> ARMOR_STAND_LEFT_ARM_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(18);
    public static final EntityDataAccessor<Rotations> ARMOR_STAND_RIGHT_ARM_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(19);
    public static final EntityDataAccessor<Rotations> ARMOR_STAND_LEFT_LEG_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(20);
    public static final EntityDataAccessor<Rotations> ARMOR_STAND_RIGHT_LEG_ROTATION = EntityDataSerializers.ROTATIONS.createAccessor(21);
}
