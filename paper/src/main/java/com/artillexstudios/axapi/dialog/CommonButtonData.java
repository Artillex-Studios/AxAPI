package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public record CommonButtonData(Component label, @Nullable Component tooltip, OptionalInt width) {

    public void encode(CompoundTag tag) {
        tag.putComponent("label", this.label);
        if (tooltip != null) {
            tag.putComponent("tooltip", this.tooltip);
        }
        this.width.ifPresent(value -> tag.putInt("width", value));
    }
}
