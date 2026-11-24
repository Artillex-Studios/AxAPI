package com.artillexstudios.axapi.dialog.body;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.wrapper.ServerWrapper;
import com.artillexstudios.axapi.utils.Optionals;
import org.jspecify.annotations.Nullable;

import java.util.OptionalInt;

public record ItemDialogBody(WrappedItemStack stack, @Nullable TextDialogBody description, boolean showDecorations, boolean showTooltip, OptionalInt width, OptionalInt height) implements DialogBody {

    public static ItemDialogBody.Builder builder(WrappedItemStack stack) {
        return new ItemDialogBody.Builder(stack);
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public void encode(CompoundTag tag) {
        tag.put("item", stack.serializeAsNBT());
        if (this.description != null) {
            tag.put("contents", ServerWrapper.INSTANCE.serializeComponentAsNBT(this.description.message()));
            if (this.description.width().isPresent()) {
                tag.putInt("width", this.description.width().getAsInt());
            }
        }

        if (!this.showDecorations) {
            tag.putBoolean("show_decorations", false);
        }

        if (!this.showTooltip) {
            tag.putBoolean("show_tooltip", false);
        }

        this.width.ifPresent(w -> tag.putInt("width", w));
        this.height.ifPresent(h -> tag.putInt("height", h));
    }

    public static final class Builder {
        private final WrappedItemStack stack;
        private TextDialogBody description = null;
        private boolean showDecorations = true;
        private boolean showInTooltip = true;
        private Integer width = null;
        private Integer height = null;

        private Builder(WrappedItemStack stack) {
            this.stack = stack;
        }

        public ItemDialogBody.Builder withDescription(TextDialogBody description) {
            this.description = description;
            return this;
        }

        public ItemDialogBody.Builder showDecorations(boolean showDecorations) {
            this.showDecorations = showDecorations;
            return this;
        }

        public ItemDialogBody.Builder showInTooltip(boolean showInTooltip) {
            this.showInTooltip = showInTooltip;
            return this;
        }

        public ItemDialogBody.Builder width(int width) {
            this.width = width;
            return this;
        }

        public ItemDialogBody.Builder height(int height) {
            this.height = height;
            return this;
        }

        public ItemDialogBody build() {
            return new ItemDialogBody(this.stack, this.description, this.showDecorations, this.showInTooltip, Optionals.ifPresent(this.width, OptionalInt::of, OptionalInt::empty), Optionals.ifPresent(this.height, OptionalInt::of, OptionalInt::empty));
        }
    }
}
