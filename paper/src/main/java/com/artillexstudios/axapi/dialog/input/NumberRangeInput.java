package com.artillexstudios.axapi.dialog.input;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public record NumberRangeInput(OptionalInt width, Component label, @Nullable String labelFormat, RangeInfo rangeInfo) implements InputControl {

    @Override
    public String getType() {
        return "number_range";
    }

    @Override
    public void encode(CompoundTag tag) {
        if (this.width.isPresent()) {
            tag.putInt("width", this.width.getAsInt());
        }

        tag.putComponent("label", this.label);
        if (this.labelFormat != null) {
            tag.putString("label_format", this.labelFormat);
        }
        this.rangeInfo.encode(tag);
    }

    public record RangeInfo(float start, float end, Optional<Float> initial, Optional<Float> step) {

        public void encode(CompoundTag tag) {
            tag.putFloat("start", this.start);
            tag.putFloat("end", this.end);
            this.initial.ifPresent(value -> tag.putFloat("initial", value));
            this.step.ifPresent(value -> tag.putFloat("step", value));
        }
    }
}
