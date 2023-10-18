package com.artillexstudios.axapi.nms.v1_18_R1.entity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class SynchedEntityData {
    private final Int2ObjectMap<net.minecraft.network.syncher.SynchedEntityData.DataItem<?>> items = new Int2ObjectOpenHashMap<>();
    private boolean isDirty = false;

    public <T> void define(EntityDataAccessor<T> accessor, T value) {
        this.createDataItem(accessor, value);
    }

    public <T> void createDataItem(EntityDataAccessor<T> accessor, T value) {
        net.minecraft.network.syncher.SynchedEntityData.DataItem<T> dataItem = new net.minecraft.network.syncher.SynchedEntityData.DataItem<>(accessor, value);

        items.put(accessor.getId(), dataItem);
    }

    private <T> net.minecraft.network.syncher.SynchedEntityData.DataItem<T> getItem(EntityDataAccessor<T> key) {
        return (net.minecraft.network.syncher.SynchedEntityData.DataItem) this.items.get(key.getId());
    }

    public <T> T get(EntityDataAccessor<T> data) {
        return this.getItem(data).getValue();
    }

    public <T> void set(EntityDataAccessor<T> key, T value) {
        this.set(key, value, false);
    }

    public <T> void set(EntityDataAccessor<T> key, T value, boolean force) {
        net.minecraft.network.syncher.SynchedEntityData.DataItem<T> datawatcher_item = this.getItem(key);

        if (force || ObjectUtils.notEqual(value, datawatcher_item.getValue())) {
            datawatcher_item.setValue(value);
            datawatcher_item.setDirty(true);
            this.isDirty = true;
        }
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public List<net.minecraft.network.syncher.SynchedEntityData.DataItem<?>> getAll() {
        List<net.minecraft.network.syncher.SynchedEntityData.DataItem<?>> list = null;

        for (net.minecraft.network.syncher.SynchedEntityData.DataItem<?> next : this.items.values()) {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(next.copy());
        }

        return list;
    }

    public List<net.minecraft.network.syncher.SynchedEntityData.DataItem<?>> packDirty() {
        List<net.minecraft.network.syncher.SynchedEntityData.DataItem<?>> list = null;

        if (isDirty) {
            for (net.minecraft.network.syncher.SynchedEntityData.DataItem<?> next : this.items.values()) {
                if (next.isDirty()) {
                    next.setDirty(false);
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    list.add(next.copy());
                }
            }
        }

        this.isDirty = false;
        return list;
    }
}
