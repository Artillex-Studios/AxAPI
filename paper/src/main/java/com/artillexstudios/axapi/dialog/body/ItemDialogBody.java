package com.artillexstudios.axapi.dialog.body;

import com.artillexstudios.axapi.items.WrappedItemStack;
import org.jspecify.annotations.Nullable;

public record ItemDialogBody(WrappedItemStack stack, @Nullable TextDialogBody description, boolean showDecorations, boolean showTooltip, int width, int height) implements DialogBody {

}
