package com.artillexstudios.axapi.loot;

import net.kyori.adventure.key.Key;

public class LootContextParam<T> {
    private final Key key;

    public LootContextParam(Key key) {
        this.key = key;
    }

    public Key key() {
        return key;
    }
}
