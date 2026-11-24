package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.Optionals;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public record CommonButtonData(Component label, @Nullable Component tooltip, OptionalInt width) {

    public static CommonButtonData.Builder builder() {
        return new CommonButtonData.Builder();
    }

    public void encode(CompoundTag tag) {
        tag.putComponent("label", this.label);
        if (tooltip != null) {
            tag.putComponent("tooltip", this.tooltip);
        }
        this.width.ifPresent(value -> tag.putInt("width", value));
    }

    public static final class Builder {
        private Component label = Component.empty();
        private Component tooltip = null;
        private Integer width = null;

        public CommonButtonData.Builder withLabel(Component label) {
            this.label = label;
            return this;
        }

        public CommonButtonData.Builder withToolTip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public CommonButtonData.Builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public CommonButtonData build() {
            return new CommonButtonData(this.label, this.tooltip, Optionals.ifPresent(this.width, OptionalInt::of, OptionalInt::empty));
        }
    }
}
