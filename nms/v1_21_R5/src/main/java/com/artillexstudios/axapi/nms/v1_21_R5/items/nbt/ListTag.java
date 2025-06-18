package com.artillexstudios.axapi.nms.v1_21_R5.items.nbt;

import com.artillexstudios.axapi.items.nbt.Tag;
import net.minecraft.nbt.CompoundTag;

public class ListTag implements com.artillexstudios.axapi.items.nbt.ListTag {
    private final net.minecraft.nbt.ListTag parent;

    public ListTag(net.minecraft.nbt.ListTag parent) {
        this.parent = parent;
    }

    @Override
    public void add(Tag tag) {
        this.parent.add(tag.getParent() instanceof CompoundTag compoundTag ? compoundTag : (net.minecraft.nbt.ListTag) tag.getParent());
    }

    @Override
    public void remove(Tag tag) {
        this.parent.remove(tag.getParent() instanceof CompoundTag compoundTag ? compoundTag : tag.getParent());
    }

    @Override
    public boolean contains(Tag tag) {
        return this.parent.contains(tag.getParent() instanceof CompoundTag compoundTag ? compoundTag : tag.getParent());
    }

    @Override
    public Object getParent() {
        return this.parent;
    }
}
