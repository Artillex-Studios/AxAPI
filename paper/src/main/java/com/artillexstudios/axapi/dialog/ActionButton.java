package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.dialog.action.Action;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;

import javax.annotation.Nullable;

public record ActionButton(CommonButtonData commonButtonData, @Nullable Action action) {

    public static ActionButton.Builder builder(CommonButtonData commonButtonData) {
        return new ActionButton.Builder(commonButtonData);
    }

    public void encode(CompoundTag tag) {
        this.commonButtonData.encode(tag);

        if (this.action != null) {
            CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
            compoundTag.putString("type", this.action.getType());
            this.action.encode(compoundTag);
            tag.put("action", compoundTag);
        }
    }

    public static final class Builder {
        private final CommonButtonData commonButtonData;
        private Action action = null;

        public Builder(CommonButtonData commonButtonData) {
            this.commonButtonData = commonButtonData;
        }

        public ActionButton.Builder withAction(Action action) {
            this.action = action;
            return this;
        }

        public ActionButton build() {
            return new ActionButton(this.commonButtonData, this.action);
        }
    }
}
