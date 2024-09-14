package com.artillexstudios.axapi.commands;

import com.artillexstudios.axapi.commands.arguments.ArgumentType;

public class CommandArgument {
    private final ArgumentType<?> type;
    private final String name;

    public CommandArgument(ArgumentType<?> type, String name) {
        this.type = type;
        this.name = name;
    }

    public ArgumentType<?> type() {
        return type;
    }

    public String name() {
        return name;
    }
}
