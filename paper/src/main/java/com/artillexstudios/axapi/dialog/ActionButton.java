package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.dialog.action.Action;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;

public record ActionButton(Component label, @Nullable Component tooltip, int width, @Nullable Action action) {

}
