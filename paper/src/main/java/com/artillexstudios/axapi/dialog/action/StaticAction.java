package com.artillexstudios.axapi.dialog.action;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.text.event.ClickEvent;

public record StaticAction(ClickEvent clickEvent) implements Action {

    @Override
    public String getType() {
        return clickEvent.action().toString();
    }

    @Override
    public void encode(CompoundTag tag) {
        // I have no clue if this is correct, this is not easy to figure out
        tag.putString(this.clickEvent.examinableName(), this.clickEvent.toString());
    }
}
