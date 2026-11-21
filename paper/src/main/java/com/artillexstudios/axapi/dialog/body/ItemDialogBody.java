package com.artillexstudios.axapi.dialog.body;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.wrapper.ServerWrapper;
import org.jspecify.annotations.Nullable;

import java.util.OptionalInt;

public record ItemDialogBody(WrappedItemStack stack, @Nullable TextDialogBody description, boolean showDecorations, boolean showTooltip, OptionalInt width, OptionalInt height) implements DialogBody {

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
        this.height.ifPresent(h -> tag.putInt("heiht", h));
    }
}
