package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
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

    void writeByte(byte value);

    byte readByte();
}
