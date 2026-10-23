package com.artillexstudios.axapi.dialog.body;

import com.artillexstudios.axapi.items.nbt.CompoundTag;

public interface DialogBody {

    String getType();

    void encode(CompoundTag tag);
}
