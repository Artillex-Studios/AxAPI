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

    public void encode(CompoundTag tag) {
        tag.putComponent("title", this.title);
        if (this.externalTitle != null) {
            tag.putComponent("external_title", this.title);
        }

        if (!this.canCloseWithEscape) {
            tag.putBoolean("can_close_with_escape", false);
        }

        if (!this.pause) {
            tag.putBoolean("can_close_with_escape", false);
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
}
