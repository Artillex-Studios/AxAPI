package com.artillexstudios.axapi.dialog.body;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.wrapper.ServerWrapper;
import net.kyori.adventure.text.Component;

import java.util.OptionalInt;

public record TextDialogBody(Component message, OptionalInt width) implements DialogBody {

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
}
