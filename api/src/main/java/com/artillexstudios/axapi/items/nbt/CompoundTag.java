package com.artillexstudios.axapi.items.nbt;

import com.artillexstudios.axapi.nms.NMSHandlers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CompoundTag {

    static CompoundTag create() {
        return NMSHandlers.getNmsHandler().newTag();
    }

    void put(String key, CompoundTag tag);

    void putByte(String key, byte value);

    void putShort(String key, short value);

    void putInt(String key, int value);

    void putLong(String key, long value);

    void putUUID(String key, UUID value);

    UUID getUUID(String key);

    boolean containsUUID(String key);

    void putFloat(String key, float value);

    void putDouble(String key, double value);

    void putString(String key, String value);

    void putByteArray(String key, byte[] value);

    void putByteArray(String key, List<Byte> value);

    void putIntArray(String key, int[] value);

    void putIntArray(String key, List<Integer> value);

    void putLongArray(String key, long[] value);

    void putLongArray(String key, List<Long> value);

    void putBoolean(String key, boolean value);

    boolean contains(String key);

    byte getByte(String key);

    short getShort(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    double getDouble(String key);

    String getString(String key);

    byte[] getByteArray(String key);

    int[] getIntArray(String key);

    long[] getLongArray(String key);

    CompoundTag getCompound(String key);

    boolean getBoolean(String key);

    void remove(String key);

    boolean isEmpty();

    Set<String> getAllKeys();

    Object getParent();
}
