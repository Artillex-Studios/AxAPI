package com.artillexstudios.axapi.commands;

import java.util.List;

public class RegisterableCommand {
    private final String[] aliases;
    private final Object instance;
    private final List<SubCommand> subCommands;

    public RegisterableCommand(String[] aliases, Object instance, List<SubCommand> subCommands) {
        this.aliases = aliases;
        this.instance = instance;
        this.subCommands = subCommands;
    }

    public String[] aliases() {
        return aliases;
    }

    public Object instance() {
        return instance;
    }

    public List<SubCommand> subCommands() {
        return subCommands;
    }
}
