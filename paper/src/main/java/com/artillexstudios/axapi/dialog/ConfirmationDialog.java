package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;

public record ConfirmationDialog(CommonDialogData data, ActionButton yes, ActionButton no) implements Dialog {

    @Override
    public String type() {
        return "confirmation";
    }

    @Override
    public void encode(CompoundTag tag) {
        this.data.encode(tag);
        CompoundTag yesTag = NMSHandlers.getNmsHandler().newTag();
        this.yes.encode(yesTag);
        tag.put("yes", yesTag);
        CompoundTag noTag = NMSHandlers.getNmsHandler().newTag();
        this.yes.encode(noTag);
        tag.put("no", noTag);
    }
}
