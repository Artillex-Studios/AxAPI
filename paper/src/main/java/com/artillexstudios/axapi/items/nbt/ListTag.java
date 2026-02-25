package com.artillexstudios.axapi.items.nbt;

public interface ListTag extends Tag {

    void add(Tag tag);

    void remove(Tag tag);

    boolean contains(Tag tag);
}
