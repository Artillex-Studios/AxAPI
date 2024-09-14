package com.artillexstudios.axapi.commands;

import com.artillexstudios.axapi.commands.arguments.Arguments;
import com.artillexstudios.axapi.commands.exception.NotCommandException;
import com.artillexstudios.axapi.nms.NMSHandlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class Commands {

    public static void register(Class<?> commandClass) {
        Object instance;
        try {
            instance = commandClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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

    private static void handleMethod(Method method, List<CommandArgument> arguments) {
        for (Parameter parameter : method.getParameters()) {
            Named name = parameter.getAnnotation(Named.class);
            arguments.add(new CommandArgument(Arguments.parse(parameter), name != null ? name.value() : parameter.getName()));
        }
    }
}
