package com.artillexstudios.axapi.dialog.input;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public record BooleanInputControl(Component label, boolean initial, @Nullable String onTrue,
                                  @Nullable String onFalse) implements InputControl {

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public void encode(CompoundTag tag) {
        tag.putComponent("label", this.label);
        if (this.initial) {
            tag.putBoolean("initial", true);
        }

        if (this.onTrue != null) {
            tag.putString("on_true", this.onTrue);
        }

        if (this.onFalse != null) {
            tag.putString("on_false", this.onFalse);
        }
    }
}
