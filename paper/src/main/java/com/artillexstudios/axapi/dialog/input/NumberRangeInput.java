package com.artillexstudios.axapi.dialog.input;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public record NumberRangeInput(int width, Component label, String labelFormat, RangeInfo rangeInfo) implements InputControl {

    public record RangeInfo(float start, float end, Optional<Float> initial, Optional<Float> step) {

    }
}
