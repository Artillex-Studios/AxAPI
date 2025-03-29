package com.artillexstudios.axapi.utils;

import net.kyori.adventure.key.Key;

public class GlobalPosition {
    private Key key;
    private BlockPosition position;

    public GlobalPosition(Key key, BlockPosition position) {
        this.key = key;
        this.position = position;
    }

    public GlobalPosition(Key key, int x, int y, int z) {
        this.key = key;
        this.position = new BlockPosition(x, y, z);
    }

    public Key key() {
        return this.key;
    }

    public void key(Key key) {
        this.key = key;
    }

    public BlockPosition position() {
        return this.position;
    }

    public void position(BlockPosition position) {
        this.position = position;
    }
}
