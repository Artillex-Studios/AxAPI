package com.artillexstudios.axapi.items.nbt;

public record HolderTag(Object parent) implements Tag {

    @Override
    public Object getParent() {
        return this.parent;
    }
}
