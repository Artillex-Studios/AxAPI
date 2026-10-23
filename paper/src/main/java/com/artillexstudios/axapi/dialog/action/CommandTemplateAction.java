package com.artillexstudios.axapi.dialog.action;

import com.artillexstudios.axapi.items.nbt.CompoundTag;

public record CommandTemplateAction(String template) implements Action {

    @Override
    public String getType() {
        return "dynamic/run_command";
    }

    @Override
    public void encode(CompoundTag tag) {
        tag.putString("template", this.template);
    }
}
