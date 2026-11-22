package com.artillexstudios.axapi.dialog.action;

import com.artillexstudios.axapi.dialog.DialogInputs;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.RandomStringGenerator;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public record CustomAllAction(Key key, @Nullable CompoundTag tag) implements Action {

    public static CustomAllAction create(DialogDataConsumer consumer) {
        String key = RandomStringGenerator.lowercase().generate(15);
        DialogInputs.register(key, consumer);
        return new CustomAllAction(Key.key("axapi_dialogs", key), null);
    }

    public interface DialogDataConsumer {

        void call(Player player, CompoundTag tag);
    }

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
