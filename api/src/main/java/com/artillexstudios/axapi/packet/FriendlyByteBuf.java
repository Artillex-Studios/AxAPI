package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.BlockPosition;
import com.artillexstudios.axapi.utils.Vector3f;
import com.artillexstudios.axapi.utils.Version;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.block.data.BlockData;

import java.util.Optional;

public interface FriendlyByteBuf {

    WrappedItemStack readItemStack();

    void writeItemStack(WrappedItemStack wrappedItemStack);

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
}
