package com.artillexstudios.axapi.dialog.body;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.wrapper.ServerWrapper;
import com.artillexstudios.axapi.utils.Optionals;
import net.kyori.adventure.text.Component;

import java.util.OptionalInt;

public record TextDialogBody(Component message, OptionalInt width) implements DialogBody {

    public static TextDialogBody.Builder builder() {
        return new TextDialogBody.Builder();
    }

    @Override
    public String getType() {
        return "plain_message";
    }

    @Override
    public void encode(CompoundTag tag) {
        tag.put("contents", ServerWrapper.INSTANCE.serializeComponentAsNBT(this.message));
        if (this.width.isPresent()) {
            tag.putInt("width", this.width.getAsInt());
        }
    }

    public static final class Builder {
        private Component message = null;
        private Integer width = null;

        public TextDialogBody.Builder withMessage(Component message) {
            this.message = message;
            return this;
        }

        public TextDialogBody.Builder width(int width) {
            this.width = width;
            return this;
        }

        public TextDialogBody build() {
            return new TextDialogBody(this.message, Optionals.ifPresent(this.width, OptionalInt::of, OptionalInt::empty));
        }
    }
}
