package com.artillexstudios.axapi.items.component.type;

import net.kyori.adventure.text.format.NamedTextColor;

public enum Rarity {
    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.YELLOW),
    RARE(NamedTextColor.AQUA),
    EPIC(NamedTextColor.LIGHT_PURPLE);

    private final NamedTextColor color;

    Rarity(NamedTextColor color) {
        this.color = color;
    }

    public NamedTextColor color() {
        return color;
    }
}
