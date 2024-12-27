package com.artillexstudios.axapi.command.builder;

import com.artillexstudios.axapi.command.annotation.Command;
import com.artillexstudios.axapi.command.exception.InvalidCommandException;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandBuilder {
    private final CommandParser parser = new CommandParser();
    private final JavaPlugin plugin;

    public CommandBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Class<?> commandClass) {
        if (!commandClass.isAnnotationPresent(Command.class)) {
            throw new InvalidCommandException("Class %s is not a command!".formatted(commandClass));
        }

        CommandTree tree = this.parser.parse(commandClass);

    }
}
