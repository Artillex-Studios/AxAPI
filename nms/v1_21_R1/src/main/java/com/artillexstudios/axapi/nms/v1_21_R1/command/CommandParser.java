package com.artillexstudios.axapi.nms.v1_21_R1.command;

import com.artillexstudios.axapi.collections.IdentityArrayMap;
import com.artillexstudios.axapi.commands.CommandArgument;
import com.artillexstudios.axapi.commands.RegisterableCommand;
import com.artillexstudios.axapi.commands.SubCommand;
import com.artillexstudios.axapi.commands.arguments.ArgumentType;
import com.artillexstudios.axapi.commands.arguments.Arguments;
import com.artillexstudios.axapi.reflection.MethodInvoker;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CommandParser {
    private static final IdentityArrayMap<ArgumentType<?>, com.mojang.brigadier.arguments.ArgumentType<?>> arguments = new IdentityArrayMap<>();

    static {
        arguments.put(Arguments.ENTITY, EntityArgument.entity());
        arguments.put(Arguments.ENTITIES, EntityArgument.entities());
        arguments.put(Arguments.PLAYER, EntityArgument.player());
        arguments.put(Arguments.PLAYERS, EntityArgument.players());
        arguments.put(Arguments.WORD, StringArgumentType.word());
        arguments.put(Arguments.STRING, StringArgumentType.string());
        arguments.put(Arguments.GREEDY, StringArgumentType.greedyString());
    }

    public static void parse(RegisterableCommand command) {
        for (String alias : command.aliases()) {
            LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(alias);

            for (SubCommand subCommand : command.subCommands()) {
                if (subCommand.noArgs()) {
                    literal.executes(source -> {
                        Method method = subCommand.method();
                        if (method.getParameterCount() == 0) {
                            MethodInvoker.invoke(method, subCommand.instance());
                        } else if (method.getParameters()[0].getType().isInstance(CommandSender.class)) {
                            MethodInvoker.invoke(method, subCommand.instance(), source.getSource().getBukkitSender());
                        } else {
                            return 0;
                        }

                        return 1;
                    });
                    continue;
                }

                for (String s : subCommand.aliases()) {
                    LiteralArgumentBuilder<CommandSourceStack> l = Commands.literal(s);
                    for (CommandArgument argument : subCommand.arguments()) {
                        RequiredArgumentBuilder<CommandSourceStack, ?> arg = Commands.argument(argument.name(), arguments.get(argument.type()));
                        l = l.then(arg);
                    }

                    l.executes(stack -> {
                        Method method = subCommand.method();
                        if (method.getParameterCount() == 0) {
                            MethodInvoker.invoke(method, subCommand.instance());
                        } else if (method.getParameters()[0].getType().isInstance(CommandSender.class)) {
                            MethodInvoker.invoke(method, subCommand.instance(), stack.getSource().getBukkitSender());
                        }

                        for (Parameter parameter : subCommand.method().getParameters()) {

                        }

                        return 1;
                    });

                    literal.then(l);
                }
            }
        }
    }
}
