package com.artillexstudios.axapi.commands.arguments;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Parameter;
import java.util.HashMap;

public class Arguments {
    private static final HashMap<Class<?>, ArgumentType<Object, Object>> types = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(Arguments.class);
    public static final ArgumentType<?, ?> PLAYER = register(Player.class, new InternalArgumentType("player"));
    public static final ArgumentType<?, ?> PLAYERS = register(Player[].class, new InternalArgumentType("players"));
    public static final ArgumentType<?, ?> ENTITY = register(Entity.class, new InternalArgumentType("entity"));
    public static final ArgumentType<?, ?> ENTITIES = register(Entity[].class, new InternalArgumentType("entities"));
    public static final ArgumentType<?, ?> STRING = register(String.class, new InternalArgumentType("string"));
    public static final ArgumentType<?, ?> INT = register(int.class, new InternalArgumentType("int"));
    public static final ArgumentType<?, ?> INTEGER = register(Integer.class, new InternalArgumentType("integer"));
    public static final ArgumentType<?, ?> DIMENSION = register(World.class, new InternalArgumentType("dimension"));
    public static final ArgumentType<?, ?> DOUBLE = register(double.class, new InternalArgumentType("double"));
    public static final ArgumentType<?, ?> DOUBLE_TYPE = register(Double.class, new InternalArgumentType("double_type"));
    public static final ArgumentType<?, ?> LONG = register(long.class, new InternalArgumentType("long"));
    public static final ArgumentType<?, ?> LONG_TYPE = register(Long.class, new InternalArgumentType("long_type"));
    public static final ArgumentType<?, ?> FLOAT = register(float.class, new InternalArgumentType("float"));
    public static final ArgumentType<?, ?> FLOAT_TYPE = register(Float.class, new InternalArgumentType("float_type"));
    public static final ArgumentType<?, ?> BOOL = register(boolean.class, new InternalArgumentType("bool"));
    public static final ArgumentType<?, ?> BOOLEAN = register(Boolean.class, new InternalArgumentType("boolean"));
    public static final ArgumentType<?, ?> GAMEMODE = register(GameMode.class, new InternalArgumentType("gamemode"));
    public static final ArgumentType<?, ?> GAME_PROFILE = register(OfflinePlayer.class, new InternalArgumentType("game_profile"));
    public static final ArgumentType<?, ?> GAME_PROFILES = register(OfflinePlayer[].class, new InternalArgumentType("game_profiles"));
    public static final ArgumentType<?, ?> LOCATION = register(Location.class, new InternalArgumentType("location"));
    public static final ArgumentType<?, ?> BLOCK = register(Block.class, new InternalArgumentType("block"));

    public static ArgumentType<?, ?> register(Class<?> clazz, ArgumentType<Object, Object> argumentType) {
        log.info("Registered new type! {}", argumentType.type());
        types.put(clazz, argumentType);
        return argumentType;
    }

    public static ArgumentType<Object, Object> parse(Parameter parameter) {
        return types.get(parameter.getType());
    }
}
