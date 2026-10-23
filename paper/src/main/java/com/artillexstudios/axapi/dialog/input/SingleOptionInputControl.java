package com.artillexstudios.axapi.dialog.input;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;

public record SingleOptionInputControl(int width, List<Entry> entries, Component label, boolean labelVisible) implements InputControl {

    public record Entry(String id, Optional<Component> display, boolean initial) {

    }
}
