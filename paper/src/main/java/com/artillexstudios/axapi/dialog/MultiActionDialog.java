package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.items.nbt.Tag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public record MultiActionDialog(CommonDialogData data, List<ActionButton> actions, @Nullable ActionButton exitAction,
                                OptionalInt columns) implements Dialog {

    @Override
    public String type() {
        return "multi_action";
    }

    @Override
    public void encode(CompoundTag tag) {
        this.data.encode(tag);
        List<Tag> tags = new ArrayList<>();
        for (ActionButton action : this.actions) {
            CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
            action.encode(compoundTag);
            tags.add(compoundTag);
        }
        tag.writeTagList("actions", tags);
        if (this.exitAction != null) {
            CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
            this.exitAction.encode(compoundTag);
            tag.put("exit_action", compoundTag);
        }
        this.columns.ifPresent(value -> tag.putInt("columns", value));
    }
}
