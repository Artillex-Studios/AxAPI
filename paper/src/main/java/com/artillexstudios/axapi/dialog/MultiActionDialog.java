package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.items.nbt.Tag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.utils.Optionals;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public record MultiActionDialog(CommonDialogData data, List<ActionButton> actions, @Nullable ActionButton exitAction,
                                OptionalInt columns) implements Dialog {

    public static MultiActionDialog.Builder builder(@NonNull CommonDialogData data) {
        return new MultiActionDialog.Builder(data);
    }

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

    public static final class Builder {
        private final CommonDialogData commonDialogData;
        private List<ActionButton> buttons = new ArrayList<>();
        private ActionButton exitAction = null;
        private Integer columns = null;

        private Builder(CommonDialogData commonDialogData) {
            this.commonDialogData = commonDialogData;
        }

        public MultiActionDialog.Builder withButton(ActionButton button) {
            this.buttons.add(button);
            return this;
        }

        public MultiActionDialog.Builder withExitAction(ActionButton exitAction) {
            this.exitAction = exitAction;
            return this;
        }

        public MultiActionDialog.Builder withColumns(int columns) {
            this.columns = columns;
            return this;
        }

        public MultiActionDialog build() {
            return new MultiActionDialog(this.commonDialogData, this.buttons, this.exitAction, Optionals.ifPresent(this.columns, OptionalInt::of, OptionalInt::empty));
        }
    }
}
