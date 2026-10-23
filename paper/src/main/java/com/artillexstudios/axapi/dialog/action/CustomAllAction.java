package com.artillexstudios.axapi.dialog.action;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.Nullable;

public record CustomAllAction(Key key, @Nullable CompoundTag tag) implements Action {

    @Override
    public String getType() {
        return "dynamic/custom";
    }

    @Override
    public void encode(CompoundTag tag) {
        tag.putString("id", this.key.asString());
        if (this.tag != null) {
            tag.put("additions", this.tag);
        }
    }
}
