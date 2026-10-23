package com.artillexstudios.axapi.dialog.input;

import com.artillexstudios.axapi.items.nbt.CompoundTag;

public interface InputControl {

    String getType();

    void encode(CompoundTag tag);
}
