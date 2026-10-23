package com.artillexstudios.axapi.dialog.body;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

public record ItemDialogBody(WrappedItemStack stack, @Nullable TextDialogBody description, boolean showDecorations, boolean showTooltip, int width, int height) implements DialogBody {

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public void encode(CompoundTag tag) {
        // TODO: implement
    }
}
