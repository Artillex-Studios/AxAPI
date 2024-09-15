package com.artillexstudios.axapi.nms.v1_21_R1.command;

import com.artillexstudios.axapi.commands.CommandArgument;
import com.artillexstudios.axapi.commands.RegisterableCommand;
import com.artillexstudios.axapi.commands.SubCommand;
import com.artillexstudios.axapi.commands.arguments.ArgumentType;
import com.artillexstudios.axapi.commands.arguments.Arguments;
import com.artillexstudios.axapi.commands.arguments.annotation.GreedyString;
import com.artillexstudios.axapi.commands.arguments.annotation.Optional;
import com.artillexstudios.axapi.commands.arguments.annotation.Ranged;
import com.artillexstudios.axapi.commands.arguments.annotation.Word;
import com.artillexstudios.axapi.reflection.MethodInvoker;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.ThrowingFunction;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CommandParser {
    private static final IdentityHashMap<ArgumentType<?>, Function<CommandArgument, Pair<com.mojang.brigadier.arguments.ArgumentType<?>, ThrowingFunction<Pair<CommandContext<CommandSourceStack>, String>, Object, CommandSyntaxException>>>> arguments = new IdentityHashMap<>();
    private static final IdentityHashMap<Class<?>, BiFunction<Object, CommandSourceStack, Object>> transformers = new IdentityHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(CommandParser.class);

    static {
        arguments.put(Arguments.ENTITY, context -> Pair.of(EntityArgument.entity(), s -> EntityArgument.getEntity(s.getKey(), s.getSecond())));
        arguments.put(Arguments.ENTITIES, context -> Pair.of(EntityArgument.entities(), s -> EntityArgument.getEntities(s.getKey(), s.getSecond())));
        arguments.put(Arguments.PLAYER, context -> Pair.of(EntityArgument.player(), s -> EntityArgument.getPlayer(s.getKey(), s.getSecond())));
        arguments.put(Arguments.PLAYERS, context -> Pair.of(EntityArgument.players(), s -> EntityArgument.getPlayers(s.getKey(), s.getSecond())));
        arguments.put(Arguments.DIMENSION, context -> Pair.of(DimensionArgument.dimension(), s -> DimensionArgument.getDimension(s.getKey(), s.getSecond())));
        arguments.put(Arguments.BOOL, context -> Pair.of(BoolArgumentType.bool(), s -> BoolArgumentType.getBool(s.getKey(), s.getSecond())));
        arguments.put(Arguments.BOOLEAN, context -> Pair.of(BoolArgumentType.bool(), s -> BoolArgumentType.getBool(s.getKey(), s.getSecond())));
        arguments.put(Arguments.GAMEMODE, context -> Pair.of(GameModeArgument.gameMode(), s -> GameModeArgument.getGameMode(s.getKey(), s.getSecond())));
        arguments.put(Arguments.GAME_PROFILE, context -> Pair.of(GameProfileArgument.gameProfile(), s -> GameProfileArgument.getGameProfiles(s.getKey(), s.getSecond()).toArray()[0]));
        arguments.put(Arguments.GAME_PROFILES, context -> Pair.of(GameProfileArgument.gameProfile(), s -> GameProfileArgument.getGameProfiles(s.getKey(), s.getSecond())));
        arguments.put(Arguments.LOCATION, context -> Pair.of(Vec3Argument.vec3(), s -> Vec3Argument.getVec3(s.getKey(), s.getSecond())));
        arguments.put(Arguments.BLOCK, context -> Pair.of(BlockPosArgument.blockPos(), s -> BlockPosArgument.getLoadedBlockPos(s.getKey(), s.getSecond())));
        arguments.put(Arguments.STRING, context -> {
            boolean greedy = context.annotation(GreedyString.class) != null;
            boolean word = context.annotation(Word.class) != null;
            return Pair.of(greedy ? StringArgumentType.greedyString() : word ? StringArgumentType.word() : StringArgumentType.string(), s -> StringArgumentType.getString(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.INT, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? IntegerArgumentType.integer() : IntegerArgumentType.integer((int) ranged.min(), (int) ranged.max()), s -> IntegerArgumentType.getInteger(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.INTEGER, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? IntegerArgumentType.integer() : IntegerArgumentType.integer((int) ranged.min(), (int) ranged.max()), s -> IntegerArgumentType.getInteger(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.DOUBLE, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? DoubleArgumentType.doubleArg() : DoubleArgumentType.doubleArg(ranged.min(), ranged.max()), s -> DoubleArgumentType.getDouble(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.DOUBLE_TYPE, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? DoubleArgumentType.doubleArg() : DoubleArgumentType.doubleArg(ranged.min(), ranged.max()), s -> DoubleArgumentType.getDouble(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.LONG, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? LongArgumentType.longArg() : LongArgumentType.longArg((long) ranged.min(), (long) ranged.max()), s -> LongArgumentType.getLong(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.LONG_TYPE, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? LongArgumentType.longArg() : LongArgumentType.longArg((long) ranged.min(), (long) ranged.max()), s -> LongArgumentType.getLong(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.FLOAT, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? FloatArgumentType.floatArg() : FloatArgumentType.floatArg((float) ranged.min(), (float) ranged.max()), s -> FloatArgumentType.getFloat(s.getKey(), s.getSecond()));
        });
        arguments.put(Arguments.FLOAT_TYPE, context -> {
            Ranged ranged = context.annotation(Ranged.class);
            return Pair.of(ranged == null ? FloatArgumentType.floatArg() : FloatArgumentType.floatArg((float) ranged.min(), (float) ranged.max()), s -> FloatArgumentType.getFloat(s.getKey(), s.getSecond()));
        });


        transformers.put(BlockPos.class, (pos, source) -> {
            BlockPos blockPos = (BlockPos) pos;
            return new Location(source.getBukkitWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ()).getBlock();
        });
        transformers.put(Vec3.class, (vec, source) -> {
            Vec3 vec3 = (Vec3) vec;
            return new Location(source.getBukkitWorld(), vec3.x(), vec3.y(), vec3.z());
        });
        transformers.put(CommandSourceStack.class, (stack, source) -> ((CommandSourceStack) stack).getBukkitSender());
        transformers.put(GameProfile.class, (profile, source) -> ((CraftServer) Bukkit.getServer()).getOfflinePlayer((GameProfile) profile));
        transformers.put(GameType.class, (type, source) -> GameMode.valueOf(((GameType) type).name()));
        transformers.put(Entity.class, (entity, stack) -> ((Entity) entity).getBukkitEntity());
        transformers.put(ServerPlayer.class, (entity, stack) -> ((ServerPlayer) entity).getBukkitEntity());
        transformers.put(ServerLevel.class, (level, stack) -> ((ServerLevel) level).getWorld());
    }

    public static void register(ArgumentType<?> arg, com.mojang.brigadier.arguments.ArgumentType<?> internal) {
        arguments.put(arg, context -> Pair.of(internal, s -> s.getKey().getArgument(s.getSecond(), arg.type())));
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
                        arguments[0] = transform(stack.getSource(), stack.getSource());
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
                for (int i = 0; i < args.size(); i++) {
                    CommandArgument argument = args.get(i);

                    com.mojang.brigadier.arguments.ArgumentType<?> argType = arguments.get(argument.type()).apply(argument).getFirst();
                    RequiredArgumentBuilder<CommandSourceStack, ?> arg = Commands.argument(argument.name(), argType);

                    Optional next;
                    if (i + 1 >= args.size()) {
                        next = null;
                    } else {
                        next = args.get(i + 1).annotation(Optional.class);
                    }

                    counter++;
                    if (counter == args.size() || next != null) {
                        arg.executes(stack -> {
                            Method method = subCommand.method();
                            Object[] arguments = new Object[method.getParameterCount()];

                            arguments[0] = transform(stack.getSource(), stack.getSource());
                            int j = 1;
                            for (CommandArgument a : subCommand.arguments()) {
                                log.info("Argument type: {}", a.type().getClass());
                                try {
                                    Object returned = CommandParser.arguments.get(a.type()).apply(a).getSecond().apply(Pair.of(stack, a.name()));
                                    log.info("Returned class type: {}", returned == null ? null : returned.getClass());
                                    arguments[j] = returned == null ? null : transform(returned, stack.getSource());
                                    j++;
                                } catch (IllegalArgumentException exception) {
                                    break;
                                }
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

    private static Object transform(Object obj, CommandSourceStack stack) {
        if (obj instanceof Collection<?> collection) {
            List<Object> objects = new ArrayList<>(collection.size());
            for (Object o : collection) {
                objects.add(transform(o, stack));
            }

            return objects;
        }

        return transformers.getOrDefault(obj.getClass(), (a, b) -> a).apply(obj, stack);
    }
}
