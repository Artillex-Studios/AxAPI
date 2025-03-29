package com.artillexstudios.shared.axapi.nbt;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CompoundTag extends Tag {

    void put(String key, Tag tag);

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

    Byte getByte(String key);

    Short getShort(String key);

    Integer getInt(String key);

    Long getLong(String key);

    Float getFloat(String key);

    Double getDouble(String key);

    String getString(String key);

    byte[] getByteArray(String key);

    int[] getIntArray(String key);

    long[] getLongArray(String key);

    CompoundTag getCompound(String key);

    ListTag getList(String key);

    Boolean getBoolean(String key);

    void remove(String key);

    boolean isEmpty();

    Set<String> getAllKeys();
}
