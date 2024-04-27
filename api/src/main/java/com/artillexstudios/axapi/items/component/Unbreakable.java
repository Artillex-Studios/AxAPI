package com.artillexstudios.axapi.items.component;

public class Unbreakable {
    private final boolean showInTooltip;

    public Unbreakable(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    public boolean showInTooltip() {
        return showInTooltip;
    }
}
