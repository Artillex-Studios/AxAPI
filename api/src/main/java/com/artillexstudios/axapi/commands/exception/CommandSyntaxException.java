package com.artillexstudios.axapi.commands.exception;

import net.kyori.adventure.text.Component;

public class CommandSyntaxException extends Exception {
    private final Component component;
    private final String input;
    private final int cursor;

    public CommandSyntaxException(Component component) {
        this.component = component;
        this.input = null;
        this.cursor = -1;
    }

    public CommandSyntaxException(Component component, String input, int cursor) {
        this.component = component;
        this.input = input;
        this.cursor = cursor;
    }

    public String input() {
        return this.input;
    }

    public int cursor() {
        return this.cursor;
    }

    public Component component() {
        return this.component;
    }
}
