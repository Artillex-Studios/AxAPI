package com.artillexstudios.axapi.dialog.action;

import com.artillexstudios.axapi.items.nbt.CompoundTag;

public interface Action {

    String getType();

    void encode(CompoundTag tag);
}
