package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.PlaceholderException;
import com.artillexstudios.axapi.placeholders.exception.PlaceholderParameterNotInContextException;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.Optionals;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.regex.Matcher;

public class PlaceholderHandler {
    private static final List<Placeholder> placeholders = new ArrayList<>();
    private static final ConcurrentLinkedQueue<PlaceholderTransformer<Object, Object>> transformers = new ConcurrentLinkedQueue<>();
    private static Placeholder[] baked = new Placeholder[0];

    static {
        registerTransformer(Player.class, OfflinePlayer.class, player -> player);
        registerTransformer(OfflinePlayer.class, Player.class, OfflinePlayer::getPlayer);
    }

    void test() {
        register("wins_<koth>", new PlaceholderArguments(new PlaceholderArgument<>("koth", null)), ctx -> {
            Object koth = ctx.argument("koth");
            return koth.toString();
        });
    }

    public static <T, Z> void registerTransformer(Class<T> fromClazz, Class<Z> toClazz, Function<T, Z> transformer) {
        transformers.add((PlaceholderTransformer<Object, Object>) new PlaceholderTransformer<>(fromClazz, toClazz, transformer));
    }

    public static void register(String placeholder, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler) {
        register(placeholder, new PlaceholderArguments(), handler, false);
    }

    public static void register(String placeholder, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler, boolean placeholderAPI) {
        register(placeholder, new PlaceholderArguments(), handler, placeholderAPI);
    }

    public static void register(String placeholder, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler, PlaceholderFormatter formatter) {
        register(placeholder, new PlaceholderArguments(), handler, formatter, false);
    }

    public static void register(String placeholder, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler, PlaceholderFormatter formatter, boolean placeholderAPI) {
        register(placeholder, new PlaceholderArguments(), handler, formatter, placeholderAPI);
    }

    public static void register(String placeholder, PlaceholderArguments arguments, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler) {
        register(placeholder, arguments, handler, DefaultPlaceholderFormatter.INSTANCE, false);
    }

    public static void register(String placeholder, PlaceholderArguments arguments, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler, boolean placeholderAPI) {
        register(placeholder, arguments, handler, DefaultPlaceholderFormatter.INSTANCE, placeholderAPI);
    }

    public static void register(String placeholder, PlaceholderArguments arguments, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler, PlaceholderFormatter formatter, boolean placeholderAPI) {
        List<String> formatted = formatter.format(placeholder);
        synchronized (placeholders) {
            for (String string : formatted) {
                placeholders.add(new Placeholder(string, arguments, placeholderAPI, handler));
            }
            placeholders.sort(Comparator.comparing(Placeholder::placeholder).reversed());
            baked = placeholders.toArray(new Placeholder[0]);
        }
    }

    public static String parse(String line) {
        return parse(line, (PlaceholderParameters) null);
    }

    public static <T> String parse(String line, Class<T> clazz, T value) {
        return parse(line, new PlaceholderParameters()
                .withParameter(clazz, value)
        );
    }

    public static String parse(String line, Object... objects) {
        return parse(line, new PlaceholderParameters()
                .withParameters(objects)
        );
    }

    public static String parseWithPlaceholderAPI(String line, PlaceholderParameters parameters) {
        String newLine = parse(line, parameters);
        if (ClassUtils.INSTANCE.classExists("me.clip.placeholderapi.PlaceholderAPI")) {
            try {
                newLine = PlaceholderAPI.setPlaceholders(Optionals.orElse(parameters.getByName("player"), parameters.resolve(Player.class)), newLine);
            } catch (PlaceholderParameterNotInContextException exception) {
                return newLine;
            }
        }

        return newLine;
    }

    public static String parse(String line, PlaceholderParameters parameters) {
        for (Placeholder placeholder : baked) {
            if (placeholder.arguments().arguments().length == 0) {
                if (line.contains(placeholder.placeholder())) {
                    try {
                        line = line.replace(placeholder.placeholder(), placeholder.handler().apply(placeholder.newContext(parameters, null)));
                    } catch (PlaceholderException exception) {
                        if (FeatureFlags.DEBUG.get()) {
                            LogUtils.warn("Placeholder parse! Line: {}", line, exception);
                        }
                    }
                }
            } else {
                line = parseInternal(placeholder, line, parameters);
            }
        }

        return line;
    }

    private static String parseInternal(Placeholder placeholder, String line, PlaceholderParameters parameters) {
        StringBuilder builder = new StringBuilder(line.length());
        Matcher match = placeholder.match(line);
        String apply;

        while (match.find()) {
            try {
                apply = placeholder.handler().apply(placeholder.newContext(parameters, match));
            } catch (PlaceholderException exception) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.warn("Placeholder parse! Line: {}", line, exception);
                }
                continue;
            }

            match.appendReplacement(builder, apply);
        }

        match.appendTail(builder);
        return builder.toString();
    }

    public static Map<String, String> mapped(Object... objects) {
        return mapped(new PlaceholderParameters()
                .withParameters(objects)
        );
    }

    public static Map<String, String> mapped(PlaceholderParameters parameters) {
        Map<String, String> map = new HashMap<>();
        for (Placeholder placeholder : baked) {
            if (placeholder.arguments().arguments().length != 0) {
                continue;
            }

            String parsed = PlaceholderHandler.parse(placeholder.placeholder(), parameters);
            if (parsed.equals(placeholder.placeholder())) {
                continue;
            }

            map.put(placeholder.placeholder(), parsed);
        }

        return map;
    }

    public static List<String> placeholders() {
        return placeholders(null, false);
    }

    public static List<String> placeholders(String prefix) {
        return placeholders(prefix, true);
    }

    public static List<String> placeholders(String prefix, boolean placeholderAPI) {
        return Arrays.stream(baked)
                .filter(placeholder -> placeholder.placeholderAPI() == placeholderAPI)
                .map(placeholder -> {
                    if (prefix == null) {
                        return placeholder.placeholder();
                    }

                    return "%" + prefix + "_" + placeholder.placeholder().replace("%", "") + "%";
                })
                .toList();
    }

    public static ConcurrentLinkedQueue<PlaceholderTransformer<Object, Object>> transformers() {
        return transformers;
    }
}
