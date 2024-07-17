package com.artillexstudios.axapi.packetentity.meta.serializer;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.utils.ParticleArguments;
import com.artillexstudios.axapi.utils.Vector3f;
import net.kyori.adventure.text.Component;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Pose;
import org.bukkit.util.EulerAngle;

import java.util.Optional;

public final class EntityDataSerializers<T> {
    public static final EntityDataSerializers<Byte> BYTE = new EntityDataSerializers<>(Type.BYTE);
    public static final EntityDataSerializers<Integer> INT = new EntityDataSerializers<>(Type.INT);
    public static final EntityDataSerializers<Float> FLOAT = new EntityDataSerializers<>(Type.FLOAT);
    public static final EntityDataSerializers<String> STRING = new EntityDataSerializers<>(Type.STRING);
    public static final EntityDataSerializers<Component> COMPONENT = new EntityDataSerializers<>(Type.COMPONENT);
    public static final EntityDataSerializers<Optional<Component>> OPTIONAL_COMPONENT = new EntityDataSerializers<>(Type.OPTIONAL_COMPONENT);
    public static final EntityDataSerializers<WrappedItemStack> ITEM_STACK = new EntityDataSerializers<>(Type.ITEM_STACK);
    public static final EntityDataSerializers<BlockState> BLOCK_STATE = new EntityDataSerializers<>(Type.BLOCK_STATE);
    public static final EntityDataSerializers<Boolean> BOOLEAN = new EntityDataSerializers<>(Type.BOOLEAN);
    public static final EntityDataSerializers<ParticleArguments> PARTICLE = new EntityDataSerializers<>(Type.PARTICLE);
    public static final EntityDataSerializers<EulerAngle> ROTATIONS = new EntityDataSerializers<>(Type.ROTATIONS);
    public static final EntityDataSerializers<Location> LOCATION = new EntityDataSerializers<>(Type.LOCATION);
    public static final EntityDataSerializers<Optional<Location>> OPTIONAL_LOCATION = new EntityDataSerializers<>(Type.OPTIONAL_LOCATION);
    public static final EntityDataSerializers<Pose> POSE = new EntityDataSerializers<>(Type.POSE);
    public static final EntityDataSerializers<Vector3f> VECTOR3 = new EntityDataSerializers<>(Type.VECTOR3);

    public final Type type;

    public EntityDataSerializers(Type type) {
        this.type = type;
    }

    public EntityDataAccessor<T> createAccessor(int id) {
        return new EntityDataAccessor<>(id, this);
    }

    @Override
    public String toString() {
        return "EntityDataSerializers{" +
                "type=" + type +
                '}';
    }

    public enum Type {
        BYTE,
        INT,
        FLOAT,
        STRING,
        COMPONENT,
        OPTIONAL_COMPONENT,
        ITEM_STACK,
        BLOCK_STATE,
        BOOLEAN,
        PARTICLE,
        ROTATIONS,
        LOCATION,
        OPTIONAL_LOCATION,
        POSE,
        VECTOR3;
    }
}
