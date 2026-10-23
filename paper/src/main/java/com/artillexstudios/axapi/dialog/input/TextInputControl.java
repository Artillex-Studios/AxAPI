package com.artillexstudios.axapi.dialog.input;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

import java.util.OptionalInt;

public record TextInputControl(OptionalInt width, Component label, boolean labelVisible, @Nullable String initial, OptionalInt maxLength,
                               @Nullable MultiLineOptions multiLineOptions) implements InputControl {

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public void encode(CompoundTag tag) {
        this.width.ifPresent(value -> tag.putInt("width", value));
        tag.putComponent("label", this.label);
        if (!this.labelVisible) {
            tag.putBoolean("label_visible", false);
        }
        if (this.initial != null) {
            tag.putString("initial", this.initial);
        }
        this.maxLength.ifPresent(value -> tag.putInt("max_length", value));
        if (this.multiLineOptions != null) {
            CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
            this.multiLineOptions.encode(compoundTag);
            tag.put("multiline", compoundTag);
        }
    }

    public record MultiLineOptions(OptionalInt maxLines, OptionalInt height) {

        public void encode(CompoundTag tag) {
            maxLines.ifPresent(value -> tag.putInt("max_lines", value));
            height.ifPresent(value -> tag.putInt("height", value));
        }
    }
}
