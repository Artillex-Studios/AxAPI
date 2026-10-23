package com.artillexstudios.axapi.dialog.input;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.items.nbt.Tag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public record SingleOptionInputControl(OptionalInt width, List<Entry> entries, Component label,
                                       boolean labelVisible) implements InputControl {

    @Override
    public String getType() {
        return "single_option";
    }

    @Override
    public void encode(CompoundTag tag) {
        this.width.ifPresent(value -> tag.putInt("width", value));
        List<Tag> tags = new ArrayList<>();
        for (Entry entry : entries) {
            CompoundTag compoundTag = NMSHandlers.getNmsHandler().newTag();
            entry.encode(compoundTag);
            tags.add(compoundTag);
        }
        tag.writeTagList("options", tags);
        tag.putComponent("label", this.label);
        if (!this.labelVisible) {
            tag.putBoolean("label_visible", false);
        }
    }

    public record Entry(String id, Optional<Component> display, boolean initial) {

        public void encode(CompoundTag tag) {
            tag.putString("id", this.id);
            this.display.ifPresent(value -> tag.putComponent("display", value));
            if (initial) {
                tag.putBoolean("initial", true);
            }
        }
    }
}
