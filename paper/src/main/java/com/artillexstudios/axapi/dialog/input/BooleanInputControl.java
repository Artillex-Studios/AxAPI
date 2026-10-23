package com.artillexstudios.axapi.dialog.input;

import net.kyori.adventure.text.Component;

public record BooleanInputControl(Component label, boolean initial, String onTrue, String onFalse) implements InputControl {

}
