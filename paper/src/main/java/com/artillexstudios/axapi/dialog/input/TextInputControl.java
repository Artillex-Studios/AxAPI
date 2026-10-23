package com.artillexstudios.axapi.dialog.input;

import net.kyori.adventure.text.Component;

import java.util.OptionalInt;

public record TextInputControl(int width, Component label, boolean labelVisible, String initial, int maxLength, MultiLineOptions multiLineOptions) implements InputControl {

    public record MultiLineOptions(OptionalInt maxLines, OptionalInt height) {

    }
}
