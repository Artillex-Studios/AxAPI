package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import org.jspecify.annotations.Nullable;

public record NoticeDialog(CommonDialogData data, @Nullable ActionButton actionButton) implements Dialog {

    @Override
    public String type() {
        return "notice";
    }

    @Override
    public void encode(CompoundTag tag) {
        this.data.encode(tag);
        if (this.actionButton != null) {
            CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
            this.actionButton.encode(compoundTag);
            tag.put("action", compoundTag);
        }
    }
}
