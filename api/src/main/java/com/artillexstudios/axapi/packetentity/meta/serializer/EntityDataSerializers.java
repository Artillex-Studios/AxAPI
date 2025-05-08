package com.artillexstudios.axapi.packetentity.meta.serializer;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.particle.ParticleData;
import com.artillexstudios.axapi.particle.ParticleOption;
import com.artillexstudios.axapi.particle.ParticleTypes;
import com.artillexstudios.axapi.utils.BlockPosition;
import com.artillexstudios.axapi.utils.Direction;
import com.artillexstudios.axapi.utils.GlobalPosition;
import com.artillexstudios.axapi.utils.Quaternion;
import com.artillexstudios.axapi.utils.Vector3f;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.VillagerData;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.kyori.adventure.text.Component;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Pose;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

public final class EntityDataSerializers {
    private static final Int2ObjectArrayMap<EntityDataSerializer<?>> SERIALIZERS = new Int2ObjectArrayMap<>();
    private static final Object2IntArrayMap<EntityDataSerializer<?>> REVERSE_SERIALIZERS = new Object2IntArrayMap<>();
    public static final EntityDataSerializer<Byte> BYTE = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Byte value) {
            buf.writeByte(value);
        }

        @Override
        public Byte read(FriendlyByteBuf buf) {
            return buf.readByte();
        }

        @Override
        public Type type() {
            return Type.BYTE;
        }
    };
    public static final EntityDataSerializer<Integer> INT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.INT;
        }
    };
    public static final EntityDataSerializer<Long> LONG = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Long value) {
            buf.writeVarLong(value);
        }

        @Override
        public Long read(FriendlyByteBuf buf) {
            return buf.readVarLong();
        }

        @Override
        public Type type() {
            return Type.LONG;
        }
    };
    public static final EntityDataSerializer<Float> FLOAT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Float value) {
            buf.writeFloat(value);
        }

        @Override
        public Float read(FriendlyByteBuf buf) {
            return buf.readFloat();
        }

        @Override
        public Type type() {
            return Type.FLOAT;
        }
    };
    public static final EntityDataSerializer<String> STRING = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, String value) {
            buf.writeUTF(value);
        }

        @Override
        public String read(FriendlyByteBuf buf) {
            return buf.readUTF();
        }

        @Override
        public Type type() {
            return Type.STRING;
        }
    };
    public static final EntityDataSerializer<Component> COMPONENT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Component value) {
            buf.writeComponent(value);
        }

        @Override
        public Component read(FriendlyByteBuf buf) {
            return buf.readComponent();
        }

        @Override
        public Type type() {
            return Type.COMPONENT;
        }
    };
    public static final EntityDataSerializer<Optional<Component>> OPTIONAL_COMPONENT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Optional<Component> value) {
            if (value.isPresent()) {
                buf.writeBoolean(true);
                buf.writeComponent(value.get());
            } else {
                buf.writeBoolean(false);
            }
        }

        @Override
        public Optional<Component> read(FriendlyByteBuf buf) {
            return buf.readBoolean() ? Optional.of(buf.readComponent()) : Optional.empty();
        }

        @Override
        public Type type() {
            return Type.OPTIONAL_COMPONENT;
        }
    };
    public static final EntityDataSerializer<WrappedItemStack> ITEM_STACK = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, WrappedItemStack value) {
            buf.writeItemStack(value);
        }

        @Override
        public WrappedItemStack read(FriendlyByteBuf buf) {
            return buf.readItemStack();
        }

        @Override
        public Type type() {
            return Type.ITEM_STACK;
        }
    };
    public static final EntityDataSerializer<BlockData> BLOCK_DATA = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, BlockData value) {
            buf.writeBlockData(value);
        }

        @Override
        public BlockData read(FriendlyByteBuf buf) {
            return buf.readBlockData();
        }

        @Override
        public Type type() {
            return Type.BLOCK_DATA;
        }
    };
    public static final EntityDataSerializer<Boolean> BOOLEAN = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Boolean value) {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean read(FriendlyByteBuf buf) {
            return buf.readBoolean();
        }

        @Override
        public Type type() {
            return Type.BOOLEAN;
        }
    };
    public static final EntityDataSerializer<ParticleData<? extends ParticleOption>> PARTICLE = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, ParticleData<? extends ParticleOption> value) {
            ParticleTypes.write((ParticleData<ParticleOption>) value, buf);
        }

        @Override
        public ParticleData<? extends ParticleOption> read(FriendlyByteBuf buf) {
            return ParticleTypes.read(buf);
        }

        @Override
        public Type type() {
            return Type.PARTICLE;
        }
    };
    public static final EntityDataSerializer<EulerAngle> ROTATIONS = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, EulerAngle value) {
            buf.writeFloat((float) value.getX());
            buf.writeFloat((float) value.getY());
            buf.writeFloat((float) value.getZ());
        }

        @Override
        public EulerAngle read(FriendlyByteBuf buf) {
            return new EulerAngle(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public Type type() {
            return Type.ROTATIONS;
        }
    };
    public static final EntityDataSerializer<BlockPosition> BLOCK_POSITION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, BlockPosition value) {
            buf.writeBlockPos(value);
        }

        @Override
        public BlockPosition read(FriendlyByteBuf buf) {
            return buf.readBlockPosition();
        }

        @Override
        public Type type() {
            return Type.LOCATION;
        }
    };
    public static final EntityDataSerializer<Optional<BlockPosition>> OPTIONAL_BLOCK_POSITION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Optional<BlockPosition> value) {
            if (value.isPresent()) {
                buf.writeBoolean(true);
                BlockPosition pos = value.get();
                buf.writeBlockPos(pos);
            } else {
                buf.writeBoolean(false);
            }
        }

        @Override
        public Optional<BlockPosition> read(FriendlyByteBuf buf) {
            return !buf.readBoolean() ? Optional.empty() : Optional.of(buf.readBlockPosition());
        }

        @Override
        public Type type() {
            return Type.OPTIONAL_LOCATION;
        }
    };
    public static final EntityDataSerializer<Pose> POSE = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Pose value) {
            buf.writeVarInt(value.ordinal());
        }

        @Override
        public Pose read(FriendlyByteBuf buf) {
            return Pose.values()[buf.readVarInt()];
        }

        @Override
        public Type type() {
            return Type.POSE;
        }
    };
    public static final EntityDataSerializer<Integer> CAT_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.CAT_VARIANT;
        }
    };
    public static final EntityDataSerializer<Integer> WOLF_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.WOLF_VARIANT;
        }
    };
    public static final EntityDataSerializer<Integer> FROG_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.INT;
        }
    };
    public static final EntityDataSerializer<Integer> PAINTING_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.INT;
        }
    };
    public static final EntityDataSerializer<Vector3f> VECTOR3 = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Vector3f value) {
            buf.writeFloat(value.x());
            buf.writeFloat(value.y());
            buf.writeFloat(value.z());
        }

        @Override
        public Vector3f read(FriendlyByteBuf buf) {
            return new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public Type type() {
            return Type.VECTOR3;
        }
    };
    public static final EntityDataSerializer<Quaternion> QUATERNION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Quaternion value) {
            buf.writeFloat(value.x());
            buf.writeFloat(value.y());
            buf.writeFloat(value.z());
            buf.writeFloat(value.w());
        }

        @Override
        public Quaternion read(FriendlyByteBuf buf) {
            return new Quaternion(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public Type type() {
            return Type.QUATERNION;
        }
    };
    public static final EntityDataSerializer<Optional<UUID>> OPTIONAL_UUID = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Optional<UUID> value) {
            if (value.isPresent()) {
                buf.writeBoolean(true);
                UUID uuid = value.get();
                buf.writeLong(uuid.getMostSignificantBits());
                buf.writeLong(uuid.getLeastSignificantBits());
            } else {
                buf.writeBoolean(false);
            }
        }

        @Override
        public Optional<UUID> read(FriendlyByteBuf buf) {
            return buf.readBoolean() ? Optional.of(new UUID(buf.readLong(), buf.readLong())) : Optional.empty();
        }

        @Override
        public Type type() {
            return Type.OPTIONAL_UUID;
        }
    };
    public static final EntityDataSerializer<CompoundTag> COMPOUND_TAG = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, CompoundTag value) {
            buf.writeNBT(value);
        }

        @Override
        public CompoundTag read(FriendlyByteBuf buf) {
            return buf.readNBT();
        }

        @Override
        public Type type() {
            return Type.COMPOUND_TAG;
        }
    };
    public static final EntityDataSerializer<Integer> SNIFFER_STATE = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.SNIFFER_STATE;
        }
    };
    public static final EntityDataSerializer<Integer> ARMADILLO_STATE = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.ARMADILLO_STATE;
        }
    };
    public static final EntityDataSerializer<List<ParticleData<? extends ParticleOption>>> PARTICLES = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, List<ParticleData<? extends ParticleOption>> value) {
            buf.writeVarInt(value.size());
            for (ParticleData<? extends ParticleOption> option : value) {
                PARTICLE.write(buf, option);
            }
        }

        @Override
        public List<ParticleData<? extends ParticleOption>> read(FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            List<ParticleData<? extends ParticleOption>> options = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                options.add(PARTICLE.read(buf));
            }

            return options;
        }

        @Override
        public Type type() {
            return Type.PARTICLES;
        }
    };
    public static final EntityDataSerializer<Direction> DIRECTION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Direction value) {
            buf.writeVarInt(value.ordinal());
        }

        @Override
        public Direction read(FriendlyByteBuf buf) {
            return Direction.values()[buf.readVarInt()];
        }

        @Override
        public Type type() {
            return Type.DIRECTION;
        }
    };
    public static final EntityDataSerializer<VillagerData> VILLAGER_DATA = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, VillagerData value) {
            buf.writeVarInt(value.type());
            buf.writeVarInt(value.profession());
            buf.writeVarInt(value.level());
        }

        @Override
        public VillagerData read(FriendlyByteBuf buf) {
            return new VillagerData(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
        }

        @Override
        public Type type() {
            return Type.VILLAGER_DATA;
        }
    };
    public static final EntityDataSerializer<OptionalInt> OPTIONAL_UNSIGNED_INT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, OptionalInt value) {
            buf.writeVarInt(value.orElse(-1) + 1);
        }

        @Override
        public OptionalInt read(FriendlyByteBuf buf) {
            int i = buf.readVarInt();
            return i == 0 ? OptionalInt.empty() : OptionalInt.of(i - 1);
        }

        @Override
        public Type type() {
            return Type.OPTIONAL_UNSIGNED_INT;
        }
    };
    public static final EntityDataSerializer<Optional<GlobalPosition>> OPTIONAL_GLOBAL_POS = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Optional<GlobalPosition> value) {
            if (value.isPresent()) {
                buf.writeBoolean(true);
                GlobalPosition pos = value.get();
                buf.writeResourceLocation(pos.key());
                buf.writeBlockPos(pos.position());
            } else {
                buf.writeBoolean(false);
            }
        }

        @Override
        public Optional<GlobalPosition> read(FriendlyByteBuf buf) {
            return buf.readBoolean() ? Optional.of(new GlobalPosition(buf.readResourceLocation(), buf.readBlockPosition())) : Optional.empty();
        }

        @Override
        public Type type() {
            return Type.OPTIONAL_GLOBAL_POS;
        }
    };

    public static final EntityDataSerializer<OptionalInt> OPTIONAL_BLOCK_DATA = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, OptionalInt value) {
            if (value.isPresent()) {
                buf.writeVarInt(value.getAsInt());
            } else {
                buf.writeVarInt(0);
            }
        }

        @Override
        public OptionalInt read(FriendlyByteBuf buf) {
            int i = buf.readVarInt();
            return i != 0 ? OptionalInt.of(i) : OptionalInt.empty();
        }

        @Override
        public Type type() {
            return Type.OPTIONAL_BLOCK_DATA;
        }
    };

    public static final EntityDataSerializer<Integer> COW_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.COW_VARIANT;
        }
    };

    public static final EntityDataSerializer<Integer> CHICKEN_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.CHICKEN_VARIANT;
        }
    };

    public static final EntityDataSerializer<Integer> WOLF_SOUND_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.WOLF_SOUND_VARIANT;
        }
    };

    public static final EntityDataSerializer<Integer> PIG_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, Integer value) {
            buf.writeVarInt(value);
        }

        @Override
        public Integer read(FriendlyByteBuf buf) {
            return buf.readVarInt();
        }

        @Override
        public Type type() {
            return Type.PIG_VARIANT;
        }
    };

    public static <T> EntityDataSerializer<T> byId(int id) {
        return (EntityDataSerializer<T>) SERIALIZERS.get(id);
    }

    public static <T> int getId(EntityDataSerializer<T> serializer) {
        return REVERSE_SERIALIZERS.getInt(serializer);
    }

    private static <T> void register(EntityDataSerializer<T> serializer) {
        int index = SERIALIZERS.size();
        SERIALIZERS.put(index, serializer);
        REVERSE_SERIALIZERS.put(serializer, index);
        if (AxPlugin.getPlugin(AxPlugin.class).flags().DEBUG.get()) {
            LogUtils.debug("Registering entitydata serializer: {}", serializer.type());
        }
    }

    static {
        register(BYTE);
        register(INT);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19_2)) {
            register(LONG);
        }
        register(FLOAT);
        register(STRING);
        register(COMPONENT);
        register(OPTIONAL_COMPONENT);
        register(ITEM_STACK);
        register(BOOLEAN);
        register(ROTATIONS);
        register(BLOCK_POSITION);
        register(OPTIONAL_BLOCK_POSITION);
        register(DIRECTION);
        register(OPTIONAL_UUID); // Optional livingentity reference in 1.21.5
        register(BLOCK_DATA);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19_3)) {
            register(OPTIONAL_BLOCK_DATA);
        }
        register(COMPOUND_TAG);
        register(PARTICLE);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
            register(PARTICLES);
        }
        register(VILLAGER_DATA);
        register(OPTIONAL_UNSIGNED_INT);
        register(POSE);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19)) {
            register(CAT_VARIANT);
            if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_4)) {
                register(COW_VARIANT);
            }
            if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
                register(WOLF_VARIANT);
            }
            if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_4)) {
                register(WOLF_SOUND_VARIANT);
            }
            register(FROG_VARIANT);
            if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_4)) {
                register(PIG_VARIANT);
                register(CHICKEN_VARIANT);
            }
            register(OPTIONAL_GLOBAL_POS);
            register(PAINTING_VARIANT);
            register(SNIFFER_STATE);
            if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4)) {
                register(ARMADILLO_STATE);
            }
            register(VECTOR3);
            register(QUATERNION);
        }
    }

    public enum Type {
        BYTE,
        INT,
        LONG,
        FLOAT,
        STRING,
        COMPONENT,
        OPTIONAL_COMPONENT,
        ITEM_STACK,
        BLOCK_DATA,
        BOOLEAN,
        PARTICLE,
        ROTATIONS,
        LOCATION,
        OPTIONAL_LOCATION,
        POSE,
        VECTOR3,
        QUATERNION,
        PARTICLES,
        ARMADILLO_STATE,
        SNIFFER_STATE,
        COMPOUND_TAG,
        OPTIONAL_UUID,
        WOLF_VARIANT,
        CAT_VARIANT,
        DIRECTION,
        VILLAGER_DATA,
        OPTIONAL_UNSIGNED_INT,
        OPTIONAL_GLOBAL_POS,
        OPTIONAL_BLOCK_DATA,
        COW_VARIANT,
        WOLF_SOUND_VARIANT,
        PIG_VARIANT,
        CHICKEN_VARIANT;
    }
}
