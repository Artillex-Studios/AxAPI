package com.artillexstudios.axapi.placeholders.refactor;

import com.artillexstudios.axapi.placeholders.PlaceholderAPIHook;

public final class PlaceholderParser {
    private volatile boolean locked = false;
    private final boolean registerPlaceholderAPI;

    public PlaceholderParser(boolean registerPlaceholderAPI) {
        this.registerPlaceholderAPI = registerPlaceholderAPI;
    }

    public synchronized void start() {
        if (this.locked) {
            throw new UnsupportedOperationException("Registration is already locked!");
        }

        this.locked = true;
        PlaceholderAPIHook placeholderAPIHook = new PlaceholderAPIHook(this, null);
    }

    public void register(String placeholder) {

    }
}
