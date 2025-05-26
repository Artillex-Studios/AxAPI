package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.placeholders.exception.PlaceholderException;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.regex.Matcher;

public class PlaceholderHandler {
    private static final FeatureFlags flags = AxPlugin.getPlugin(AxPlugin.class).flags();
    private static final List<Placeholder> placeholders = new ArrayList<>();
    private static final ConcurrentLinkedQueue<PlaceholderTransformer<Object, Object>> transformers = new ConcurrentLinkedQueue<>();

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
        register(placeholder, new PlaceholderArguments(), handler);
    }

    public static void register(String placeholder, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler, PlaceholderFormatter formatter) {
        register(placeholder, new PlaceholderArguments(), handler, formatter);
    }

    public static void register(String placeholder, PlaceholderArguments arguments, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler) {
        register(placeholder, arguments, handler, DefaultPlaceholderFormatter.INSTANCE);
    }

    public static void register(String placeholder, PlaceholderArguments arguments, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler, PlaceholderFormatter formatter) {
        List<String> formatted = formatter.format(placeholder);
        synchronized (placeholders) {
            for (String string : formatted) {
                placeholders.add(new Placeholder(string, arguments, handler));
            }
            placeholders.sort(Comparator.comparing(Placeholder::placeholder).reversed());
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

    public static String parse(String line, PlaceholderParameters parameters) {
        synchronized (placeholders) {
            for (Placeholder placeholder : placeholders) {
                Matcher match = placeholder.match(line);
                if (!match.find()) {
                    if (flags.DEBUG.get()) {
                        LogUtils.warn("Matcher no find! Line: {}, placeholder: {}, regex: {}", line, placeholder.placeholder(), placeholder.pattern());
                    }
                    continue;
                }

                try {
                    line = match.replaceAll(placeholder.handler().apply(placeholder.newContext(parameters, match)));
                } catch (PlaceholderException exception) {
                    if (flags.DEBUG.get()) {
                        LogUtils.warn("Placeholder parse! Line: {}", line, exception);
                    }
                    continue;
                }
            }

            return line;
        }
    }

    public static Map<String, String> mapped(Object... objects) {
        return mapped(new PlaceholderParameters()
                .withParameters(objects)
        );
    }

    public static Map<String, String> mapped(PlaceholderParameters parameters) {
        Map<String, String> map = new HashMap<>();
        synchronized (placeholders) {
            for (Placeholder placeholder : placeholders) {
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
    }

    public static List<String> placeholders() {
        return placeholders(null);
    }

    public static List<String> placeholders(String prefix) {
        synchronized (placeholders) {
            return placeholders.stream()
                    .map(placeholder -> {
                        if (prefix == null) {
                            return placeholder.placeholder();
                        }

                        return "%" + prefix + "_" + placeholder.placeholder().replace("%", "") + "%";
                    })
                    .toList();
        }
    }

    public static ConcurrentLinkedQueue<PlaceholderTransformer<Object, Object>> transformers() {
        return transformers;
    }
}
