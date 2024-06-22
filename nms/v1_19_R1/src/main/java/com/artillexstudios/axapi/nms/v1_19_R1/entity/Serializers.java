package com.artillexstudios.axapi.nms.v1_19_R1.entity;

import com.artillexstudios.axapi.packetentity.meta.serializer.EntityDataAccessor;
import com.artillexstudios.axapi.packetentity.meta.serializer.EntityDataSerializers;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R1.CraftParticle;
import org.bukkit.util.EulerAngle;

import java.util.EnumMap;
import java.util.Optional;

public class Serializers {
    private static final FastFieldAccessor nmsStack = FastFieldAccessor.forClassField(com.artillexstudios.axapi.nms.v1_19_R1.items.WrappedItemStack.class, "parent");
    private static final EnumMap<EntityDataSerializers.Type, Transformer<?>> typeTransformers = new EnumMap<>(EntityDataSerializers.Type.class);

    static {
        typeTransformers.put(EntityDataSerializers.Type.BYTE, new Transformer<Byte>() {
            @Override
            public Byte transform(Object other) {
                return (Byte) other;
            }

            @Override
            public EntityDataSerializer<Byte> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.BYTE;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.INT, new Transformer<Integer>() {
            @Override
            public Integer transform(Object other) {
                return (Integer) other;
            }

            @Override
            public EntityDataSerializer<Integer> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.INT;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.FLOAT, new Transformer<Float>() {
            @Override
            public Float transform(Object other) {
                return (Float) other;
            }

            @Override
            public EntityDataSerializer<Float> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.FLOAT;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.STRING, new Transformer<String>() {
            @Override
            public String transform(Object other) {
                return (String) other;
            }

            @Override
            public EntityDataSerializer<String> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.STRING;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.COMPONENT, new Transformer<Component>() {
            @Override
            public Component transform(Object other) {
                return ComponentSerializer.INSTANCE.toVanilla((net.kyori.adventure.text.Component) other);
            }

            @Override
            public EntityDataSerializer<Component> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.COMPONENT;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.OPTIONAL_COMPONENT, new Transformer<Optional<Component>>() {
            @Override
            public Optional<Component> transform(Object other) {
                return ((Optional<net.kyori.adventure.text.Component>) other).map(ComponentSerializer.INSTANCE::toVanilla);
            }

            @Override
            public EntityDataSerializer<Optional<Component>> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.OPTIONAL_COMPONENT;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.ITEM_STACK, new Transformer<ItemStack>() {

            @Override
            public ItemStack transform(Object other) {
                return nmsStack.get(other);
            }

            @Override
            public EntityDataSerializer<ItemStack> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.ITEM_STACK;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.BOOLEAN, new Transformer<Boolean>() {
            @Override
            public Boolean transform(Object other) {
                return (Boolean) other;
            }

            @Override
            public EntityDataSerializer<Boolean> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.PARTICLE, new Transformer<ParticleOptions>() {
            @Override
            public ParticleOptions transform(Object other) {
                return CraftParticle.toNMS((Particle) other, null);
            }

            @Override
            public EntityDataSerializer<ParticleOptions> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.PARTICLE;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.ROTATIONS, new Transformer<Rotations>() {
            private static Rotations toRotations(EulerAngle eulerAngle) {
                return new Rotations((float) Math.toDegrees(eulerAngle.getX()), (float) Math.toDegrees(eulerAngle.getY()), (float) Math.toDegrees(eulerAngle.getZ()));
            }

            @Override
            public Rotations transform(Object other) {
                return toRotations((EulerAngle) other);
            }

            @Override
            public EntityDataSerializer<Rotations> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.ROTATIONS;
            }
        });
    }


    public static <T, Z> Transformer<Z> transformer(EntityDataAccessor<T> accessor) {
        return (Transformer<Z>) typeTransformers.get(accessor.serializers().type);
    }

    public interface Transformer<Z> {

        Z transform(Object other);

        EntityDataSerializer<Z> serializer();
    }
}
