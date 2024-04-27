package com.artillexstudios.axapi.items.component;

import org.bukkit.Color;

public class DyedColor {
    private final Color color;
    private final boolean showInTooltip;

    public DyedColor(Color color, boolean showInTooltip) {
        this.color = color;
        this.showInTooltip = showInTooltip;
    }

    public boolean showInTooltip() {
        return showInTooltip;
    }

    public Color color() {
        return color;
    }

    public int rgb() {
        return color.asRGB();
    }
}
