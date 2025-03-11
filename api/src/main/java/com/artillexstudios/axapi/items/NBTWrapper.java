package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.items.nbt.Tag;
import org.bukkit.inventory.ItemStack;

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

    public void build() {
        this.wrappedItemStack.set(DataComponents.customData(), this.tag);
        this.wrappedItemStack.finishEdit();
    }
}
