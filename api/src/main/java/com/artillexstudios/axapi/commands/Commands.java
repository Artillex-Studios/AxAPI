package com.artillexstudios.axapi.commands;

import com.artillexstudios.axapi.commands.arguments.ArgumentType;
import com.artillexstudios.axapi.commands.arguments.Arguments;
import com.artillexstudios.axapi.commands.exception.NotCommandException;
import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.command.CommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class Commands {

    private static final Logger log = LoggerFactory.getLogger(Commands.class);

    public static void register(Class<?> commandClass) {
        Object instance;
        try {
            instance = commandClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Command command = commandClass.getAnnotation(Command.class);
        if (command == null) {
            throw new NotCommandException(commandClass.getName() + " is not a command! Annotate it with @Command!");
        }

        List<SubCommand> subCommands = new ArrayList<>();
        for (Method method : commandClass.getDeclaredMethods()) {
            NoArgs noArgs = method.getDeclaredAnnotation(NoArgs.class);
            Command subCommand = method.getDeclaredAnnotation(Command.class);
            List<CommandArgument> arguments = new ArrayList<>();
            handleMethod(method, arguments);
            subCommands.add(new SubCommand(subCommand == null ? null : subCommand.value(), instance, method, arguments, noArgs != null));
        }

        NMSHandlers.getNmsHandler().registerCommand(new RegisterableCommand(command.value(), instance, subCommands));
    }

    public static <T, Z> void registerArgumentType(ArgumentType<T, Z> argumentType) {
        Arguments.register(argumentType.type(), argumentType);
        NMSHandlers.getNmsHandler().registerArgumentType(argumentType);
    }

    private static void handleMethod(Method method, List<CommandArgument> arguments) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (i == 0 && CommandSender.class.isAssignableFrom(parameter.getType())) {
                continue;
            }

            Named name = parameter.getAnnotation(Named.class);
            log.info("Parameter: {}", name == null ? parameter.getName() : name.value());
            arguments.add(new CommandArgument(Arguments.parse(parameter), name != null ? name.value() : parameter.getName(), parameter.getAnnotations(), parameter.getType()));
        }
    }
}
