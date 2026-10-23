package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.dialog.body.DialogBody;
import com.artillexstudios.axapi.dialog.input.Input;
import net.kyori.adventure.text.Component;

import java.util.List;

public record CommonDialogData(Component title, Component externalTitle, boolean canCloseWithEscape, boolean pause, DialogAction action, List<DialogBody> body, List<Input> inputs) {
}
