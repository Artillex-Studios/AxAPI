package com.artillexstudios.axapi.nms.v1_21_R1.command;

import com.artillexstudios.axapi.commands.CommandArgument;
import com.artillexstudios.axapi.commands.RegisterableCommand;
import com.artillexstudios.axapi.commands.SubCommand;
import com.artillexstudios.axapi.commands.arguments.ArgumentType;
import com.artillexstudios.axapi.commands.arguments.Arguments;
import com.artillexstudios.axapi.reflection.MethodInvoker;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.ThrowingFunction;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;

public class CommandParser {
    private static final IdentityHashMap<ArgumentType<?>, Pair<com.mojang.brigadier.arguments.ArgumentType<?>, ThrowingFunction<Pair<CommandContext<CommandSourceStack>, String>, Object, CommandSyntaxException>>> arguments = new IdentityHashMap<>();
    private static final IdentityHashMap<Class<?>, Function<Object, Object>> transformers = new IdentityHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(CommandParser.class);

    static {
        arguments.put(Arguments.ENTITY, Pair.of(EntityArgument.entity(), s -> EntityArgument.getEntity(s.getKey(), s.getSecond())));
        arguments.put(Arguments.ENTITIES, Pair.of(EntityArgument.entities(), s -> EntityArgument.getEntities(s.getKey(), s.getSecond())));
        arguments.put(Arguments.PLAYER, Pair.of(EntityArgument.player(), s -> EntityArgument.getPlayer(s.getKey(), s.getSecond())));
        arguments.put(Arguments.PLAYERS, Pair.of(EntityArgument.players(), s -> EntityArgument.getPlayers(s.getKey(), s.getSecond())));
        arguments.put(Arguments.WORD, Pair.of(StringArgumentType.word(), s -> StringArgumentType.getString(s.getKey(), s.getSecond())));
        arguments.put(Arguments.STRING, Pair.of(StringArgumentType.string(), s -> StringArgumentType.getString(s.getKey(), s.getSecond())));
        arguments.put(Arguments.GREEDY, Pair.of(StringArgumentType.greedyString(), s -> StringArgumentType.getString(s.getKey(), s.getSecond())));

        transformers.put(CommandSourceStack.class, stack -> ((CommandSourceStack) stack).getBukkitSender());
        transformers.put(String.class, string -> string);
        transformers.put(Entity.class, entity -> ((Entity) entity).getBukkitEntity());
        transformers.put(ServerPlayer.class, entity -> ((ServerPlayer) entity).getBukkitEntity());
        transformers.put(Collection.class, collection -> {
            List<Object> l = new ArrayList<>();
            Collection<Object> c = (Collection<Object>) collection;
            for (Object o : c) {
                l.add(transformers.get(o.getClass()).apply(o));
            }

            return l;
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> parse(RegisterableCommand command) {
        String alias = command.aliases()[0];

        LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(alias);

        for (SubCommand subCommand : command.subCommands()) {
            if (subCommand.noArgs()) {
                literal = literal.executes(stack -> {
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
                ArgumentBuilder<CommandSourceStack, ?> l = Commands.literal(s);
                ArgumentBuilder<CommandSourceStack, ?> last = l;

                List<CommandArgument> args = subCommand.arguments();
                int counter = 0;
                for (CommandArgument argument : args) {
                    com.mojang.brigadier.arguments.ArgumentType<?> argType = arguments.get(argument.type()).getFirst();
                    log.info("Argtype: {};  {}", argType, argument.type());
                    RequiredArgumentBuilder<CommandSourceStack, ?> arg = Commands.argument(argument.name(), argType);

                    counter++;
                    if (counter == args.size()) {
                        arg.executes(stack -> {
                            Method method = subCommand.method();
                            Object[] arguments = new Object[method.getParameterCount()];
                            log.info("Stack source type: {} transformers: {}", stack.getSource().getClass(), transformers);
                            arguments[0] = transformers.get(stack.getSource().getClass()).apply(stack.getSource());
                            int i = 1;
                            for (CommandArgument a : subCommand.arguments()) {
                                log.info("Argument type: {}", a.type().getClass());
                                Object returned = CommandParser.arguments.get(a.type()).getSecond().apply(Pair.of(stack, a.name()));
                                log.info("Returned class type: {}", returned == null ? null : returned.getClass());
                                arguments[i] = returned == null ? null : transformers.get(returned.getClass()).apply(returned);
                                i++;
                            }

                            try {
                                MethodInvoker.invoke(method, subCommand.instance(), arguments);
                            } catch (Exception exception) {
                                log.error("An unexpected error occurred while executing command {}", argument, exception);
                                return 0;
                            }
                            return 1;
                        });
                    }

                    last.then(arg);
                    last = arg;
                }

                literal = literal.then(l);
            }
        }

        return literal;
    }
}
