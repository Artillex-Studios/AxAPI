package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.dialog.body.DialogBody;
import com.artillexstudios.axapi.dialog.input.Input;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.items.nbt.Tag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record CommonDialogData(Component title, @Nullable Component externalTitle, boolean canCloseWithEscape, boolean pause,
                               DialogAction action, List<DialogBody> body, List<Input> inputs) {

    public static CommonDialogData.Builder builder() {
        return new CommonDialogData.Builder();
    }

    public void encode(CompoundTag tag) {
        tag.putComponent("title", this.title);
        if (this.externalTitle != null) {
            tag.putComponent("external_title", this.title);
        }

        if (!this.canCloseWithEscape) {
            tag.putBoolean("can_close_with_escape", false);
        }

        if (!this.pause) {
            tag.putBoolean("pause", false);
        }

        if (this.action != DialogAction.CLOSE) {
            tag.putString("after_action", this.action.getActionName());
        }

        if (!this.body.isEmpty()) {
            List<Tag> tagList = new ArrayList<>();
            for (DialogBody dialogBody : this.body) {
                CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
                compoundTag.putString("type", dialogBody.getType());
                dialogBody.encode(compoundTag);
                tagList.add(compoundTag);
            }

            tag.writeCompactTagList("body", tagList);
        }

        if (!this.inputs.isEmpty()) {
            List<Tag> tagList = new ArrayList<>();
            for (Input input : this.inputs) {
                CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
                input.encode(compoundTag);
                tagList.add(compoundTag);
            }

            tag.writeTagList("inputs", tagList);
        }
    }

    public static final class Builder {
        private final List<DialogBody> body = new ArrayList<>();
        private final List<Input> inputs = new ArrayList<>();
        private Component title = Component.empty();
        private Component externalTitle = null;
        private boolean canCloseWithEscape = true;
        private boolean pause = false;
        private DialogAction action = DialogAction.CLOSE;

        public CommonDialogData.Builder withTitle(Component title) {
            this.title = title;
            return this;
        }

        public CommonDialogData.Builder withExternalTitle(Component externalTitle) {
            this.externalTitle = externalTitle;
            return this;
        }

        public CommonDialogData.Builder canCloseWithEscape(boolean canCloseWithEscape) {
            this.canCloseWithEscape = canCloseWithEscape;
            return this;
        }

        public CommonDialogData.Builder pause(boolean pause) {
            this.pause = pause;
            return this;
        }

        public CommonDialogData.Builder withAction(DialogAction action) {
            this.action = action;
            return this;
        }

        public CommonDialogData.Builder withBody(DialogBody body) {
            this.body.add(body);
            return this;
        }

        public CommonDialogData.Builder withInput(Input input) {
            this.inputs.add(input);
            return this;
        }

        public CommonDialogData build() {
            return new CommonDialogData(this.title, this.externalTitle, this.canCloseWithEscape, this.pause, this.action, this.body, this.inputs);
        }
    }
}
