package com.artillexstudios.axapi.packetentity.meta.serializer;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.utils.RotationType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;

import java.util.Optional;

public final class EntityDataSerializers<T> {
    public static final EntityDataSerializers<Byte> BYTE = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Integer> INT = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Float> FLOAT = new EntityDataSerializers<>();
    public static final EntityDataSerializers<String> STRING = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Component> COMPONENT = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Optional<Component>> OPTIONAL_COMPONENT = new EntityDataSerializers<>();
    public static final EntityDataSerializers<WrappedItemStack> ITEM_STACK = new EntityDataSerializers<>();
    public static final EntityDataSerializers<BlockState> BLOCK_STATE = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Boolean> BOOLEAN = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Particle> PARTICLE = new EntityDataSerializers<>();
    public static final EntityDataSerializers<RotationType> ROTATIONS = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Location> LOCATION = new EntityDataSerializers<>();
    public static final EntityDataSerializers<Optional<Location>> OPTIONAL_LOCATION = new EntityDataSerializers<>();

    public EntityDataAccessor<T> createAccessor(int id) {
        return new EntityDataAccessor<>(id, this);
    }
}
