package com.artillexstudios.axapi.dialog.input;

import com.artillexstudios.axapi.items.nbt.CompoundTag;

public record Input(String key, InputControl control) {

    public void encode(CompoundTag tag) {
        tag.putString("type", this.control.getType());
        tag.putString("key", this.key);
        this.control.encode(tag);
    }
}
