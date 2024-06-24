package com.artillexstudios.axapi.packetentity.meta;

import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import com.artillexstudios.axapi.packetentity.meta.serializer.EntityDataAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public final class Metadata {
    private final Int2ObjectMap<DataItem<?>> items = new Int2ObjectOpenHashMap<>();
    private volatile boolean dirty = false;

    public <T> void define(EntityDataAccessor<T> accessor, T value) {
        this.createDataItem(accessor, value);
    }

    public <T> void createDataItem(EntityDataAccessor<T> accessor, T value) {
        DataItem<T> dataItem = new DataItem<>(accessor, value);

        items.put(accessor.id(), dataItem);
    }

    private <T> DataItem<T> getItem(EntityDataAccessor<T> key) {
        return (DataItem) this.items.get(key.id());
    }

    public <T> T get(EntityDataAccessor<T> data) {
        return this.getItem(data).getValue();
    }

    public <T> void set(EntityDataAccessor<T> key, T value) {
        this.set(key, value, false);
    }

    public <T> void set(EntityDataAccessor<T> key, T value, boolean force) {
        DataItem<T> datawatcher_item = this.getItem(key);

        if (force || ObjectUtils.notEqual(value, datawatcher_item.getValue())) {
            datawatcher_item.setValue(value);
            datawatcher_item.setDirty(true);
            this.dirty = true;
        }
    }

    public List<DataItem<?>> getNonDefaultValues() {
        List<DataItem<?>> list = null;

        for (DataItem<?> next : this.items.values()) {
            if (!next.isSetToDefault()) {
                if (list == null) {
                    list = new ArrayList<>();
                }

                list.add(next.copy());
            }
        }

        return list;
    }

    public List<DataItem<?>> packForNameUpdate() {
        List<DataItem<?>> list = null;

        for (DataItem<?> next : this.items.values()) {
            if (next.getAccessor().id() == Accessors.CUSTOM_NAME.id()) {
                list = new ArrayList<>(1);
                list.add(next.copy());
                break;
            }
        }

        return list;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public List<DataItem<?>> getAll() {
        List<DataItem<?>> list = null;

        for (DataItem<?> next : this.items.values()) {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(next.copy());
        }

        return list;
    }

    public List<DataItem<?>> packDirty() {
        List<DataItem<?>> list = null;

        if (dirty) {
            for (DataItem<?> next : this.items.values()) {
                if (next.isDirty()) {
                    next.setDirty(false);
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    list.add(next.copy());
                }
            }
        }

        this.dirty = false;
        return list;
    }

    public static class DataItem<T> {
        final EntityDataAccessor<T> accessor;
        final T original;
        volatile T value;
        private volatile boolean dirty;

        public DataItem(EntityDataAccessor<T> data, T value) {
            this.accessor = data;
            this.value = value;
            this.original = value;
            this.dirty = true;
        }

        public EntityDataAccessor<T> getAccessor() {
            return this.accessor;
        }

        public T getValue() {
            return this.value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public boolean isDirty() {
            return this.dirty;
        }

        public void setDirty(boolean dirty) {
            this.dirty = dirty;
        }

        public boolean isSetToDefault() {
            return this.original.equals(this.value);
        }

        public DataItem<T> copy() {
            return new DataItem<>(this.accessor, value);
        }

        @Override
        public String toString() {
            return "DataItem{" +
                    "accessor=" + accessor +
                    ", value=" + value +
                    ", original=" + original +
                    ", dirty=" + dirty +
                    '}';
        }
    }
}
