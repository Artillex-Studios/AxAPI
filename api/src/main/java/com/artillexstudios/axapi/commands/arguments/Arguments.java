package com.artillexstudios.axapi.commands.arguments;

import com.artillexstudios.axapi.collections.IdentityArrayMap;
import com.artillexstudios.axapi.commands.arguments.annotation.GreedyString;
import com.artillexstudios.axapi.commands.arguments.annotation.Word;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class Arguments {
    private static final IdentityArrayMap<Class<?>, ArgumentType<?>> types = new IdentityArrayMap<>();
    private static final IdentityArrayMap<Class<? extends Annotation>, ArgumentType<?>> annotations = new IdentityArrayMap<>();
    public static final ArgumentType<?> PLAYER = register(Player.class, new InternalArgumentType());
    public static final ArgumentType<?> PLAYERS = register(Player[].class, new InternalArgumentType());
    public static final ArgumentType<?> ENTITY = register(Entity.class, new InternalArgumentType());
    public static final ArgumentType<?> ENTITIES = register(Entity[].class, new InternalArgumentType());
    public static final ArgumentType<?> STRING = register(String.class, new InternalArgumentType());
    public static final ArgumentType<?> GREEDY = registerAnnotated(GreedyString.class, new InternalArgumentType());
    public static final ArgumentType<?> WORD = registerAnnotated(Word.class, new InternalArgumentType());

    public static ArgumentType<?> register(Class<?> clazz, ArgumentType<?> argumentType) {
        types.put(clazz, argumentType);
        return argumentType;
    }

    public static ArgumentType<?> registerAnnotated(Class<? extends Annotation> clazz, ArgumentType<?> argumentType) {
        annotations.put(clazz, argumentType);
        return argumentType;
    }

    public static ArgumentType<?> parse(Parameter parameter) {
        ArgumentType<?> type = null;
        for (Annotation annotation : parameter.getAnnotations()) {
            ArgumentType<?> t = annotations.get(annotation.annotationType());
            if (t != null) {
                type = t;
                break;
            }
        }

        if (type == null) {
            type = types.get(parameter.getType());
        }

        return type;
    }
}
