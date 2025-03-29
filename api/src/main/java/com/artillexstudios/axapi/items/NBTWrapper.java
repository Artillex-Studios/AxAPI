package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.items.nbt.ListTag;
import com.artillexstudios.axapi.items.nbt.Tag;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

public class NBTWrapper {
    private final WrappedItemStack wrappedItemStack;
    private final CompoundTag tag;

    public NBTWrapper(WrappedItemStack wrappedItemStack) {
        this.wrappedItemStack = wrappedItemStack;
        this.tag = wrappedItemStack.get(DataComponents.customData());
    }

    public NBTWrapper(ItemStack itemStack) {
        this(WrappedItemStack.wrap(itemStack));
    }

    public static NBTWrapper wrap(WrappedItemStack wrappedItemStack) {
        return new NBTWrapper(wrappedItemStack);
    }

    public static NBTWrapper wrap(ItemStack stack) {
        return new NBTWrapper(stack);
    }

    public NBTWrapper set(String key, int value) {
        this.tag.putInt(key, value);
        return this;
    }

    public NBTWrapper set(String key, short value) {
        this.tag.putShort(key, value);
        return this;
    }

    public NBTWrapper set(String key, String value) {
        this.tag.putString(key, value);
        return this;
    }

    public NBTWrapper set(String key, float value) {
        this.tag.putFloat(key, value);
        return this;
    }

    public NBTWrapper set(String key, double value) {
        this.tag.putDouble(key, value);
        return this;
    }

    public NBTWrapper set(String key, byte value) {
        this.tag.putByte(key, value);
        return this;
    }

    public NBTWrapper set(String key, UUID value) {
        this.tag.putUUID(key, value);
        return this;
    }

    public NBTWrapper set(String key, long value) {
        this.tag.putLong(key, value);
        return this;
    }

    public NBTWrapper set(String key, Tag value) {
        this.tag.put(key, value);
        return this;
    }

    public NBTWrapper set(String key, byte[] value) {
        this.tag.putByteArray(key, value);
        return this;
    }

    public NBTWrapper set(String key, int[] value) {
        this.tag.putIntArray(key, value);
        return this;
    }

    public NBTWrapper set(String key, long[] value) {
        this.tag.putLongArray(key, value);
        return this;
    }

    public Byte getByte(String key) {
        return this.tag.getByte(key);
    }

    public Short getShort(String key) {
        return this.tag.getShort(key);
    }

    public Integer getInt(String key) {
        return this.tag.getInt(key);
    }

    public Long getLong(String key) {
        return this.tag.getLong(key);
    }

    public String getString(String key) {
        return this.tag.getString(key);
    }

    public byte[] getByteArray(String key) {
        return this.tag.getByteArray(key);
    }

    public long[] getLongArray(String key) {
        return this.tag.getLongArray(key);
    }

    public int[] getIntArray(String key) {
        return this.tag.getIntArray(key);
    }

    public Float getFloat(String key) {
        return this.tag.getFloat(key);
    }

    public Double getDouble(String key) {
        return this.tag.getDouble(key);
    }

    public UUID getUUID(String key) {
        return this.tag.getUUID(key);
    }

    public ListTag getList(String key) {
        return this.tag.getList(key);
    }

    public CompoundTag getCompound(String key) {
        return this.tag.getCompound(key);
    }

    public boolean contains(String key) {
        return this.tag.contains(key);
    }

    public boolean containsUUID(String key) {
        return this.tag.containsUUID(key);
    }

    public Set<String> getAllKeys() {
        return this.tag.getAllKeys();
    }

    public void build() {
        this.wrappedItemStack.set(DataComponents.customData(), this.tag);
        this.wrappedItemStack.finishEdit();
    }
}
