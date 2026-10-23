package com.artillexstudios.axapi.dialog;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record MultiActionDialog(CommonDialogData data, List<ActionButton> actions, @Nullable ActionButton exitAction, int columns) {
}
