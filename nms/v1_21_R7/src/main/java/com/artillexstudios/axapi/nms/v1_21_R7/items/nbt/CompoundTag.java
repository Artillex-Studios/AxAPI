package com.artillexstudios.axapi.nms.v1_21_R7.items.nbt;

import com.artillexstudios.axapi.items.nbt.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CompoundTag implements com.artillexstudios.axapi.items.nbt.CompoundTag {
    private final net.minecraft.nbt.CompoundTag parent;

    public CompoundTag(net.minecraft.nbt.CompoundTag tag) {
        this.parent = tag;
    }

    @Override
    public void put(String key, Tag tag) {
        this.parent.put(key, tag.getParent() instanceof net.minecraft.nbt.CompoundTag compoundTag ? compoundTag : (net.minecraft.nbt.ListTag) tag.getParent());
    }

    @Override
    public void putByte(String key, byte value) {
        this.parent.putByte(key, value);
    }

    @Override
    public void putShort(String key, short value) {
        this.parent.putShort(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        this.parent.putInt(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        this.parent.putLong(key, value);
    }

    @Override
    public void putUUID(String key, UUID value) {
        if (this.parent.contains(key + "Most") && this.parent.contains(key + "Least")) {
            this.parent.remove(key + "Most");
            this.parent.remove(key + "Least");
        }

        this.putIntArray(key, uuidToIntArray(value));
    }

    @Override
    public UUID getUUID(String key) {
        return this.contains(key + "Most") && this.contains(key + "Least") ? new UUID(this.getLong(key + "Most"), this.getLong(key + "Least")) : this.contains(key) ? uuidFromIntArray(this.getIntArray(key)) : null;
    }

    @Override
    public boolean containsUUID(String key) {
        return (this.contains(key + "Least") && this.contains(key + "Most")) || this.contains(key);
    }

    @Override
    public void putFloat(String key, float value) {
        this.parent.putFloat(key, value);
    }

    @Override
    public void putDouble(String key, double value) {
        this.parent.putDouble(key, value);
    }

    @Override
    public void putString(String key, String value) {
        this.parent.putString(key, value);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        this.parent.putByteArray(key, value);
    }

    @Override
    public void putByteArray(String key, List<Byte> value) {
        byte[] bytes = new byte[value.size()];
        int i = 0;
        for (Byte b : value) {
            bytes[i] = b;
            i++;
        }

        this.parent.putByteArray(key, bytes);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        this.parent.putIntArray(key, value);
    }

    @Override
    public void putIntArray(String key, List<Integer> value) {
        int[] ints = new int[value.size()];
        int i = 0;
        for (Integer integer : value) {
            ints[i] = integer;
            i++;
        }

        this.parent.putIntArray(key, ints);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        this.parent.putLongArray(key, value);
    }

    @Override
    public void putLongArray(String key, List<Long> value) {
        long[] longs = new long[value.size()];
        int i = 0;
        for (Long l : value) {
            longs[i] = l;
            i++;
        }

        this.parent.putLongArray(key, longs);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        this.parent.putBoolean(key, value);
    }

    @Override
    public boolean contains(String key) {
        return this.parent.contains(key);
    }

    @Override
    public Byte getByte(String key) {
        return this.parent.getByte(key).orElse(null);
    }

    @Override
    public Short getShort(String key) {
        return this.parent.getShort(key).orElse(null);
    }

    @Override
    public Integer getInt(String key) {
        return this.parent.getInt(key).orElse(null);
    }

    @Override
    public Long getLong(String key) {
        return this.parent.getLong(key).orElse(null);
    }

    @Override
    public Float getFloat(String key) {
        return this.parent.getFloat(key).orElse(null);
    }

    @Override
    public Double getDouble(String key) {
        return this.parent.getDouble(key).orElse(null);
    }

    @Override
    public String getString(String key) {
        return this.parent.getString(key).orElse(null);
    }

    @Override
    public byte[] getByteArray(String key) {
        return this.parent.getByteArray(key).orElse(null);
    }

    @Override
    public int[] getIntArray(String key) {
        return this.parent.getIntArray(key).orElse(null);
    }

    @Override
    public long[] getLongArray(String key) {
        return this.parent.getLongArray(key).orElse(null);
    }

    @Override
    public com.artillexstudios.axapi.items.nbt.CompoundTag getCompound(String key) {
        return this.parent.getCompound(key).map(CompoundTag::new).orElse(null);
    }

    @Override
    public ListTag getList(String key) {
        return this.parent.getList(key).map(ListTag::new).orElse(null);
    }

    @Override
    public Boolean getBoolean(String key) {
        return this.parent.getBoolean(key).orElse(null);
    }

    @Override
    public void remove(String key) {
        this.parent.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return this.parent.isEmpty();
    }

    @Override
    public Set<String> getAllKeys() {
        return this.parent.keySet();
    }

    @Override
    public net.minecraft.nbt.CompoundTag getParent() {
        return this.parent;
    }

    public static UUID uuidFromIntArray(int[] array) {
        return new UUID((long) array[0] << 32 | (long) array[1] & 4294967295L, (long) array[2] << 32 | (long) array[3] & 4294967295L);
    }

    public static int[] uuidToIntArray(UUID uuid) {
        long l = uuid.getMostSignificantBits();
        long m = uuid.getLeastSignificantBits();
        return leastMostToIntArray(l, m);
    }

    private static int[] leastMostToIntArray(long uuidMost, long uuidLeast) {
        return new int[]{(int) (uuidMost >> 32), (int) uuidMost, (int) (uuidLeast >> 32), (int) uuidLeast};
    }
}
