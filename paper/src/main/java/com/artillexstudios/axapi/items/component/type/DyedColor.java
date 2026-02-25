package com.artillexstudios.axapi.items.component.type;

import org.bukkit.Color;

public record DyedColor(Color color, boolean showInTooltip) {

    public int rgb() {
        return color.asRGB();
    }
}
