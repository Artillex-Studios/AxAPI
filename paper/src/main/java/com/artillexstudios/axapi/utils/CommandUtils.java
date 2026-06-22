package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.reflection.FieldAccessor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import java.util.Map;

public final class CommandUtils {
    private static final FieldAccessor commandMapAccessor = FieldAccessor.builder()
            .withClass(Bukkit.getServer().getClass())
            .withField("commandMap")
            .build();
    private static final FieldAccessor knownCommandsAccessor = FieldAccessor.builder()
            .withClass(SimpleCommandMap.class)
            .withField("knownCommands")
            .build();

    public static boolean isRegistered(String command) {
        Object commandMap = commandMapAccessor.get(Bukkit.getServer());
        Map<String, Command> commands = knownCommandsAccessor.getUnchecked(commandMap);
        return commands.containsKey(command);
    }
}
