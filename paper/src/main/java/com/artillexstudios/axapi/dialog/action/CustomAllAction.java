package com.artillexstudios.axapi.dialog.action;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import net.kyori.adventure.key.Key;

public record CustomAllAction(Key key, CompoundTag tag) implements Action {
}
