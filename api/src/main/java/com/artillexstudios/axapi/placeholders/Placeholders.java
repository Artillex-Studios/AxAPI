package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.utils.LogUtils;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class Placeholders {
    private static final AxPlugin plugin = AxPlugin.getPlugin(AxPlugin.class);
    private static final HashMap<String, ThrowingFunction<Context, String, ParameterNotInContextException>> placeholderAPIOfflinePlayers = new HashMap<>();
    private static final HashMap<String, ThrowingFunction<Context, String, ParameterNotInContextException>> placeholderAPIOnlinePlayers = new HashMap<>();
    private static final HashMap<String, ThrowingFunction<Context, String, ParameterNotInContextException>> internalOfflinePlayers = new HashMap<>();
    private static final HashMap<String, ThrowingFunction<Context, String, ParameterNotInContextException>> internalOnlinePlayers = new HashMap<>();
    private static final IdentityHashMap<Class<?>, Pair<Class<?>, ThrowingFunction<Object, Object, ParameterNotInContextException>>> transformers = new IdentityHashMap<>();
    private static boolean locked = false;

    public static <T, Z> void registerTransformer(Class<T> fromClazz, Class<Z> toClazz, ThrowingFunction<T, Z, ParameterNotInContextException> transformer) {
        if (locked) {
            LogUtils.error("Placeholder registration has already been locked!");
            return;
        }

        transformers.put(toClazz, Pair.of(fromClazz, (ThrowingFunction<Object, Object, ParameterNotInContextException>) transformer));
    }

    public static void register(String placeholder, ThrowingFunction<Context, String, ParameterNotInContextException> function) {
        register(placeholder, function, ParseContext.BOTH);
    }

    public static Map<String, String> asMap(Context.Builder context) {
        List<String> placeholders = placeholders(context.context());
        Map<String, String> replacements = new HashMap<>(placeholders.size());
        for (String placeholder : placeholders) {
            String output = Placeholders.parse(placeholder, context);
            if (output == null || output.equals(placeholder)) {
                continue;
            }

            replacements.put(placeholder, output);
        }

        return replacements;
    }

    public static List<String> placeholders(ParseContext context) {
        List<String> placeholders = new ArrayList<>();
        if (context == ParseContext.PLACEHOLDER_API || context == ParseContext.BOTH) {
            for (String s : placeholderAPIOnlinePlayers.keySet()) {
                placeholders.add("%" + plugin.flags().PLACEHOLDER_API_IDENTIFIER.get() + "_" + s + "%");
            }

            for (String s : placeholderAPIOfflinePlayers.keySet()) {
                placeholders.add("%" + plugin.flags().PLACEHOLDER_API_IDENTIFIER.get() + "_" + s + "%");
            }
        }

        if (context == ParseContext.INTERNAL || context == ParseContext.BOTH) {
            placeholders.addAll(internalOfflinePlayers.keySet());
            placeholders.addAll(internalOnlinePlayers.keySet());
        }

        return placeholders;
    }

    public static void register(String placeholder, ThrowingFunction<Context, String, ParameterNotInContextException> function, ParseContext context) {
        register(placeholder, function, context, ResolutionType.OFFLINE);
    }

    public static void register(String placeholder, ThrowingFunction<Context, String, ParameterNotInContextException> function, ParseContext context, ResolutionType resolutionType) {
        if (locked) {
            LogUtils.error("Placeholder registration has already been locked!");
            return;
        }

        if (context == ParseContext.PLACEHOLDER_API || context == ParseContext.BOTH) {
            if (resolutionType == ResolutionType.ONLINE) {
                placeholderAPIOnlinePlayers.put(placeholder, function);
            } else {
                placeholderAPIOfflinePlayers.put(placeholder, function);
            }
        }

        if (context == ParseContext.INTERNAL || context == ParseContext.BOTH) {
            placeholder = "%" + placeholder + "%";
            if (resolutionType == ResolutionType.ONLINE) {
                internalOnlinePlayers.put(placeholder, function);
            } else {
                internalOfflinePlayers.put(placeholder, function);
            }
        }
    }

    public static String parse(String string, Context.Builder builder) {
        HashMap<String, ThrowingFunction<Context, String, ParameterNotInContextException>> inContext = match(builder);
        Context context = builder.build();

        if (builder.context() == ParseContext.PLACEHOLDER_API) {
            if (builder.resolutionType() == ResolutionType.ONLINE) {
                for (Map.Entry<String, ThrowingFunction<Context, String, ParameterNotInContextException>> entry : placeholderAPIOnlinePlayers.entrySet()) {
                    if (entry.getKey().equals(string)) {
                        try {
                            return entry.getValue().apply(context);
                        } catch (ParameterNotInContextException exception) {
                            return string;
                        }
                    }
                }
            } else {
                for (Map.Entry<String, ThrowingFunction<Context, String, ParameterNotInContextException>> entry : placeholderAPIOfflinePlayers.entrySet()) {
                    if (entry.getKey().equals(string)) {
                        try {
                            return entry.getValue().apply(context);
                        } catch (ParameterNotInContextException exception) {
                            return string;
                        }
                    }
                }
            }

            return string;
        } else if (builder.context() == ParseContext.BOTH) {
            if (builder.resolutionType() == ResolutionType.ONLINE) {
                for (Map.Entry<String, ThrowingFunction<Context, String, ParameterNotInContextException>> entry : placeholderAPIOnlinePlayers.entrySet()) {
                    if (entry.getKey().equals(string)) {
                        try {
                            string = entry.getValue().apply(context);
                            break;
                        } catch (ParameterNotInContextException exception) {
                            break;
                        }
                    }
                }
            } else {
                for (Map.Entry<String, ThrowingFunction<Context, String, ParameterNotInContextException>> entry : placeholderAPIOfflinePlayers.entrySet()) {
                    if (entry.getKey().equals(string)) {
                        try {
                            string = entry.getValue().apply(context);
                            break;
                        } catch (ParameterNotInContextException exception) {
                            break;
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, ThrowingFunction<Context, String, ParameterNotInContextException>> entry : inContext.entrySet()) {
            try {
                string = string.replace(entry.getKey(), entry.getValue().apply(context));
            } catch (ParameterNotInContextException ignored) {
            }
        }

        return string;
    }

    private static HashMap<String, ThrowingFunction<Context, String, ParameterNotInContextException>> match(Context.Builder builder) {
        HashMap<String, ThrowingFunction<Context, String, ParameterNotInContextException>> placeholders = new HashMap<>();
        if (builder.context() == ParseContext.PLACEHOLDER_API || builder.context() == ParseContext.BOTH) {
            if (builder.resolutionType() == ResolutionType.ONLINE) {
                placeholders.putAll(placeholderAPIOnlinePlayers);
            } else {
                placeholders.putAll(placeholderAPIOfflinePlayers);
            }
        }

        if (builder.context() == ParseContext.INTERNAL || builder.context() == ParseContext.BOTH) {
            if (builder.resolutionType() == ResolutionType.ONLINE) {
                placeholders.putAll(internalOnlinePlayers);
            } else {
                placeholders.putAll(internalOfflinePlayers);
            }
        }

        return placeholders;
    }

    public static void lock() {
        if (locked) {
            LogUtils.error("Placeholder registration has already been locked!");
            return;
        }

        locked = true;
    }

    static IdentityHashMap<Class<?>, Pair<Class<?>, ThrowingFunction<Object, Object, ParameterNotInContextException>>> transformers() {
        return transformers;
    }
}
