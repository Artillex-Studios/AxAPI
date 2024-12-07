package com.artillexstudios.axapi.nms.v1_20_R2.entity;

import com.artillexstudios.axapi.packetentity.meta.serializer.EntityDataAccessor;
import com.artillexstudios.axapi.packetentity.meta.serializer.EntityDataSerializers;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.ParticleArguments;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R2.CraftParticle;
import org.bukkit.util.EulerAngle;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.Optional;

public class Serializers {
    private static final FastFieldAccessor nmsStack = FastFieldAccessor.forClassField(com.artillexstudios.axapi.nms.v1_20_R2.items.WrappedItemStack.class, "parent");
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
                ParticleArguments arguments = (ParticleArguments) other;
                return CraftParticle.createParticleParam(arguments.particle(), arguments.data());
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

        typeTransformers.put(EntityDataSerializers.Type.POSE, new Transformer<Pose>() {
            private static final Pose[] poses = Pose.values();

            @Override
            public Pose transform(Object other) {
                return poses[((org.bukkit.entity.Pose) other).ordinal()];
            }

            @Override
            public EntityDataSerializer<Pose> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.POSE;
            }
        });

        typeTransformers.put(EntityDataSerializers.Type.VECTOR3, new Transformer<Vector3f>() {
            @Override
            public Vector3f transform(Object other) {
                com.artillexstudios.axapi.utils.Vector3f vector3f = (com.artillexstudios.axapi.utils.Vector3f) other;
                return new Vector3f(vector3f.x(), vector3f.y(), vector3f.z());
            }

            @Override
            public EntityDataSerializer<Vector3f> serializer() {
                return net.minecraft.network.syncher.EntityDataSerializers.VECTOR3;
            }
        });
    }


    public static <T, Z> Transformer<Z> transformer(EntityDataAccessor<T> accessor) {
        return (Transformer<Z>) typeTransformers.get(accessor.serializers().type());
    }

    public interface Transformer<Z> {

        Z transform(Object other);

        EntityDataSerializer<Z> serializer();
    }
}
