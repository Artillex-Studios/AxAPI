package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.utils.Vector3d;
import com.artillexstudios.axapi.utils.Vector3f;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.position.BlockPosition;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.block.data.BlockData;

import java.util.Optional;
import java.util.UUID;

public interface FriendlyByteBuf {

    static FriendlyByteBuf alloc() {
        return NMSHandlers.getNmsHandler().newBuf();
    }

    default WrappedItemStack readItemStack() {
        return this.readItemStack(WrappedItemStack.CodecData.OPTIONAL_STREAM_CODEC);
    }

    WrappedItemStack readItemStack(WrappedItemStack.CodecData codecData);

    default void writeItemStack(WrappedItemStack wrappedItemStack) {
        this.writeItemStack(wrappedItemStack, WrappedItemStack.CodecData.OPTIONAL_STREAM_CODEC);
    }

    void writeItemStack(WrappedItemStack wrappedItemStack, WrappedItemStack.CodecData codecData);

    byte[] readByteArray();

    byte[] readByteArray(int maxSize);

    void writeByteArray(byte[] bytes);

    Component readComponent();

    void writeComponent(Component component);

    int readVarInt();

    void writeVarInt(int varInt);

    CompoundTag readNBT();

    void writeNBT(CompoundTag tag);

    String readUTF();

    String readUTF(int length);

    void writeUTF(String utf);

    Key readResourceLocation();

    void writeResourceLocation(Key key);

    void writeFloat(float value);

    float readFloat();

    void writeShort(int value);

    short readShort();

    void writeDouble(double value);

    double readDouble();

    void writeBoolean(boolean value);

    boolean readBoolean();

    void writeInt(int value);

    int readInt();

    void writeVarLong(long value);

    long readVarLong();

    void writeLong(long value);

    long readLong();

    void writeByte(int value);

    byte readByte();

    short readUnsignedByte();

    void readerIndex(int index);

    int readerIndex();

    void writerIndex(int index);

    int writerIndex();

    WrappedItemStack readItemCost();

    void writeItemCost(WrappedItemStack itemCost);

    Optional<WrappedItemStack> readOptionalItemCost();

    void writeOptionalItemCost(Optional<WrappedItemStack> itemCost);

    int readUnsignedShort();

    void writeBlockData(BlockData blockData);

    BlockData readBlockData();

    Vector3d readLpVec3();

    void writeLpVec3(Vector3d vector);

    void writeBytes(FriendlyByteBuf buf);

    FriendlyByteBuf readBytes(int length);

    FriendlyByteBuf copy();

    FriendlyByteBuf slice(int beginIndex, int length);

    int readableBytes();

    void release();


    default <T extends Enum<T>> T readEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[this.readVarInt()];
    }

    default <T extends Enum<T>> void writeEnum(T constant) {
        this.writeVarInt(constant.ordinal());
    }

    default Vector3f readVector3f() {
        return new Vector3f(this.readFloat(), this.readFloat(), this.readFloat());
    }

    default void writeVector3f(Vector3f value) {
        this.writeFloat(value.x());
        this.writeFloat(value.y());
        this.writeFloat(value.z());
    }

    default void writeBlockPos(BlockPosition blockPosition) {
        this.writeLong(blockPosition.asLong());
    }

    default BlockPosition readBlockPosition() {
        return BlockPosition.of(this.readLong());
    }

    default int readContainerId() {
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_2)) {
            return this.readVarInt();
        }

        return this.readUnsignedByte();
    }

    default void writeContainerId(int containerId) {
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_2)) {
            this.writeVarInt(containerId);
        } else {
            this.writeByte((byte) containerId);
        }
    }

    default void writePacketType(PacketType packetType) {
        int packetId = ClientboundPacketTypes.forPacketType(packetType);
        if (packetId == -1) {
            packetId = ServerboundPacketTypes.forPacketType(packetType);
        }

        if (packetId == -1) {
            throw new IllegalArgumentException();
        }

        this.writeVarInt(packetId);
    }

    default void writeUUID(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }

    default UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }

    default int[] readVarIntArray() {
        int readableBytes = this.readableBytes();
        int size = this.readVarInt();
        if (size > readableBytes) {
            throw new IllegalStateException();
        }

        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = this.readVarInt();
        }

        return array;
    }

    default void versionedWriteLpVec3(Vector3d vector) {
        if (Version.getServerVersion().isOlderThanOrEqualTo(Version.v1_21_6)) {
            this.writeShort((int) (Math.clamp(vector.x(), -3.9, 3.9) * 8000.0));
            this.writeShort((int) (Math.clamp(vector.y(), -3.9, 3.9) * 8000.0));
            this.writeShort((int) (Math.clamp(vector.z(), -3.9, 3.9) * 8000.0));
        } else {
            this.writeLpVec3(vector);
        }
    }

    default Vector3d versionedReadLpVec3() {
        if (Version.getServerVersion().isOlderThanOrEqualTo(Version.v1_21_6)) {
            return new Vector3d(this.readShort(), this.readShort(), this.readShort());
        } else {
            return this.readLpVec3();
        }
    }

    default void writeVarIntArray(int[] array) {
        this.writeVarInt(array.length);
        for (int i : array) {
            this.writeVarInt(i);
        }
    }
}
