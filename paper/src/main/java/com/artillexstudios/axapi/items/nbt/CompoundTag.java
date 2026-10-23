package com.artillexstudios.axapi.items.nbt;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.nms.wrapper.ServerWrapper;
import net.kyori.adventure.text.Component;

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

    default void writeTagList(String key, List<Tag> tag) {
        ListTag listTag = NMSHandlers.getNmsHandler().newListTag();
        for (Tag entry : tag) {
            listTag.add(entry);
        }

        this.put(key, listTag);
    }

    default void writeCompactTagList(String key, List<Tag> tag) {
        if (tag.size() == 1) {
            this.put(key, tag.getFirst());
        } else {
            this.writeTagList(key, tag);
        }
    }

    default void putComponent(String key, Component component) {
        this.put(key, ServerWrapper.INSTANCE.serializeComponentAsNBT(component));
    }
}
