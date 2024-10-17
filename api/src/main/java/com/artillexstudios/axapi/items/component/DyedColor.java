package com.artillexstudios.axapi.items.component;

import org.bukkit.Color;

public record DyedColor(Color color, boolean showInTooltip) {

    public int rgb() {
        return color.asRGB();
    }
}
