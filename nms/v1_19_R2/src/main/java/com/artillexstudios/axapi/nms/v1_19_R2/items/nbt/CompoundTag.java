package com.artillexstudios.axapi.nms.v1_19_R2.items.nbt;

import java.util.List;
import java.util.UUID;

public class CompoundTag implements com.artillexstudios.axapi.items.nbt.CompoundTag {
    private final net.minecraft.nbt.CompoundTag parent;

    public CompoundTag(net.minecraft.nbt.CompoundTag tag) {
        this.parent = tag;
    }

    @Override
    public void put(String key, com.artillexstudios.axapi.items.nbt.CompoundTag tag) {
        parent.put(key, (net.minecraft.nbt.CompoundTag) tag.getParent());
    }

    @Override
    public void putByte(String key, byte value) {
        parent.putByte(key, value);
    }

    @Override
    public void putShort(String key, short value) {
        parent.putShort(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        parent.putInt(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        parent.putLong(key, value);
    }

    @Override
    public void putUUID(String key, UUID value) {
        parent.putUUID(key, value);
    }

    @Override
    public UUID getUUID(String key) {
        if (!containsUUID(key)) {
            return null;
        }

        return parent.getUUID(key);
    }

    @Override
    public boolean containsUUID(String key) {
        return parent.hasUUID(key);
    }

    @Override
    public void putFloat(String key, float value) {
        parent.putFloat(key, value);
    }

    @Override
    public void putDouble(String key, double value) {
        parent.putDouble(key, value);
    }

    @Override
    public void putString(String key, String value) {
        parent.putString(key, value);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        parent.putByteArray(key, value);
    }

    @Override
    public void putByteArray(String key, List<Byte> value) {
        parent.putByteArray(key, value);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        parent.putIntArray(key, value);
    }

    @Override
    public void putIntArray(String key, List<Integer> value) {
        parent.putIntArray(key, value);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        parent.putLongArray(key, value);
    }

    @Override
    public void putLongArray(String key, List<Long> value) {
        parent.putLongArray(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        parent.putBoolean(key, value);
    }

    @Override
    public boolean contains(String key) {
        return parent.contains(key);
    }

    @Override
    public byte getByte(String key) {
        return parent.getByte(key);
    }

    @Override
    public short getShort(String key) {
        return parent.getShort(key);
    }

    @Override
    public int getInt(String key) {
        return parent.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return parent.getLong(key);
    }

    @Override
    public float getFloat(String key) {
        return parent.getFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return parent.getDouble(key);
    }

    @Override
    public String getString(String key) {
        return parent.getString(key);
    }

    @Override
    public byte[] getByteArray(String key) {
        return parent.getByteArray(key);
    }

    @Override
    public int[] getIntArray(String key) {
        return parent.getIntArray(key);
    }

    @Override
    public long[] getLongArray(String key) {
        return parent.getLongArray(key);
    }

    @Override
    public com.artillexstudios.axapi.items.nbt.CompoundTag getCompound(String key) {
        return new CompoundTag(parent.getCompound(key));
    }

    @Override
    public boolean getBoolean(String key) {
        return parent.getBoolean(key);
    }

    @Override
    public void remove(String key) {
        parent.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }

    public net.minecraft.nbt.CompoundTag getParent() {
        return parent;
    }
}
