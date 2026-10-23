package com.artillexstudios.axapi.dialog.body;

import net.kyori.adventure.text.Component;

public record TextDialogBody(Component message, int width) implements DialogBody {


}
