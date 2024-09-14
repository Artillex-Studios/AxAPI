package com.artillexstudios.axapi.nms.v1_21_R1.command;

import com.artillexstudios.axapi.collections.IdentityArrayMap;
import com.artillexstudios.axapi.commands.CommandArgument;
import com.artillexstudios.axapi.commands.RegisterableCommand;
import com.artillexstudios.axapi.commands.SubCommand;
import com.artillexstudios.axapi.commands.arguments.ArgumentType;
import com.artillexstudios.axapi.commands.arguments.Arguments;
import com.artillexstudios.axapi.reflection.MethodInvoker;
import com.artillexstudios.axapi.utils.Pair;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Method;
import java.util.function.Function;

public class CommandParser {
    private static final IdentityArrayMap<ArgumentType<?>, Pair<com.mojang.brigadier.arguments.ArgumentType<?>, Function<Pair<CommandContext<CommandSourceStack>, String>, Object>>> arguments = new IdentityArrayMap<>();
    private static final IdentityArrayMap<Class<?>, Function<Object, Object>> transformers = new IdentityArrayMap<>();

    static {
        arguments.put(Arguments.ENTITY, Pair.of(EntityArgument.entity(), s -> {
            try {
                return EntityArgument.getEntity(s.getKey(), s.getSecond());
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }));
        arguments.put(Arguments.ENTITIES, Pair.of(EntityArgument.entities(), s -> {
            try {
                return EntityArgument.getEntities(s.getKey(), s.getSecond());
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }));
        arguments.put(Arguments.PLAYER, Pair.of(EntityArgument.player(), s -> {
            try {
                return EntityArgument.getPlayer(s.getKey(), s.getSecond());
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }));
        arguments.put(Arguments.PLAYERS, Pair.of(EntityArgument.players(), s -> {
            try {
                return EntityArgument.getPlayers(s.getKey(), s.getSecond());
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }));
        arguments.put(Arguments.WORD, Pair.of(StringArgumentType.word(), s -> StringArgumentType.getString(s.getKey(), s.getSecond())));
        arguments.put(Arguments.STRING, Pair.of(StringArgumentType.string(), s -> StringArgumentType.getString(s.getKey(), s.getSecond())));
        arguments.put(Arguments.GREEDY, Pair.of(StringArgumentType.greedyString(), s -> StringArgumentType.getString(s.getKey(), s.getSecond())));

        transformers.put(CommandSourceStack.class, stack -> ((CommandSourceStack) stack).getBukkitSender());
        transformers.put(Entity.class, entity -> ((Entity) entity).getBukkitEntity());
    }

    public static LiteralArgumentBuilder<CommandSourceStack> parse(RegisterableCommand command) {
        String alias = command.aliases()[0];

        LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(alias);

        for (SubCommand subCommand : command.subCommands()) {
            if (subCommand.noArgs()) {
                literal.executes(stack -> {
                    Method method = subCommand.method();
                    if (method.getParameterCount() == 0) {
                        MethodInvoker.invoke(method, subCommand.instance());
                    } else {
                        Object[] arguments = new Object[method.getParameterCount()];
                        arguments[0] = transformers.get(stack.getSource().getClass()).apply(stack.getSource());
                        MethodInvoker.invoke(method, subCommand.instance(), arguments);
                    }

                    return 1;
                });
                continue;
            }

            for (String s : subCommand.aliases()) {
                LiteralArgumentBuilder<CommandSourceStack> l = Commands.literal(s);
                literal.then(l);

                for (CommandArgument argument : subCommand.arguments()) {
                    RequiredArgumentBuilder<CommandSourceStack, ?> arg = Commands.argument(argument.name(), arguments.get(argument.type()).getFirst());
                    l = l.then(arg);
                }

                l.executes(stack -> {
                    Method method = subCommand.method();
                    Object[] arguments = new Object[method.getParameterCount()];
                    arguments[0] = transformers.get(stack.getSource().getClass()).apply(stack.getSource());
                    int i = 1;
                    for (CommandArgument argument : subCommand.arguments()) {
                        Object returned = CommandParser.arguments.get(argument.type()).getSecond().apply(Pair.of(stack, argument.name()));
                        arguments[i] = transformers.get(returned.getClass()).apply(returned);
                        i++;
                    }

                    MethodInvoker.invoke(method, subCommand.instance(), arguments);
                    return 1;
                });
            }
        }

        return literal;
    }
}
