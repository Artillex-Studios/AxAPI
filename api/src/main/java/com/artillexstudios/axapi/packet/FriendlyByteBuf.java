package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.Version;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

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

    void writeUTF(String utf);

    Key readResourceLocation();

    void writeResourceLocation(Key key);

    void writeFloat(float value);

    float readFloat();

    void writeShort(short value);

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
}
