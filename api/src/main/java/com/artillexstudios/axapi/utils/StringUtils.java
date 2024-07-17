package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("(?<=&#)[0-9a-fA-F]{6}|(?<=#)[0-9a-fA-F]{6}");
    private static final FastFieldAccessor TEXT = FastFieldAccessor.forClassField(Matcher.class, "text");
    private static final ObjectImmutableList<Pair<String, String>> COLOR_FORMATS = ObjectImmutableList.of(
            Pair.of("&0", "<black>"),
            Pair.of("&1", "<dark_blue>"),
            Pair.of("&2", "<dark_green>"),
            Pair.of("&3", "<dark_aqua>"),
            Pair.of("&4", "<dark_red>"),
            Pair.of("&5", "<dark_purple>"),
            Pair.of("&6", "<gold>"),
            Pair.of("&7", "<gray>"),
            Pair.of("&8", "<dark_gray>"),
            Pair.of("&9", "<blue>"),
            Pair.of("&a", "<green>"),
            Pair.of("&b", "<aqua>"),
            Pair.of("&c", "<red>"),
            Pair.of("&d", "<light_purple>"),
            Pair.of("&e", "<yellow>"),
            Pair.of("&f", "<white>"),
            Pair.of("&l", "<b>"),
            Pair.of("&m", "<st>"),
            Pair.of("&n", "<u>"),
            Pair.of("&o", "<i>"),
            Pair.of("&r", "<reset>")
    );
    private static final Cache<String, String> COLOR_CACHE = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofSeconds(30))
            .maximumSize(200)
            .build();
    public static MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(StandardTags.defaults())
            .build();
    public static LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .useUnusualXRepeatedCharacterHexFormat()
            .character('\u00a7')
            .hexColors()
            .build();

    public static Component format(@NotNull String input, @NotNull Map<String, String> replacements) {
        return format(input, ItemBuilder.mapResolvers(replacements));
    }

    public static Component format(@NotNull String input, @NotNull TagResolver... resolvers) {
        String formatted = COLOR_CACHE.get(input, str -> {
            String toFormat = str;
            for (Pair<String, String> placeholder : COLOR_FORMATS) {
                toFormat = toFormat.replace(placeholder.getFirst(), placeholder.getSecond());
            }

            toFormat = replaceAll(HEX_PATTERN.matcher(toFormat), fo -> "<#" + fo.group(0) + ">").replace("&#", "");
            toFormat = ItemBuilder.toTagResolver(toFormat, resolvers);

            return toFormat;
        });

        if (formatted == null) {
            return Component.empty();
        }

        return MINI_MESSAGE.deserialize(formatted, resolvers).applyFallbackStyle(TextDecoration.ITALIC.withState(false));
    }

    public static String formatToString(@NotNull String string, @NotNull TagResolver... resolvers) {
        return ChatColor.translateAlternateColorCodes('&', LEGACY_COMPONENT_SERIALIZER.serialize(format(string, resolvers)));
    }

    public static String formatToString(@NotNull String string, @NotNull Map<String, String> replacements) {
        return formatToString(string, ItemBuilder.mapResolvers(replacements));
    }

    @NotNull
    public static List<Component> formatList(@NotNull List<String> list, TagResolver... resolvers) {
        List<Component> newList = new ArrayList<>(list.size());
        for (String line : list) {
            newList.add(format(line, resolvers));
        }

        return newList;
    }

    @NotNull
    public static List<Component> formatList(@NotNull List<String> list, Map<String, String> replacements) {
        return formatList(list, ItemBuilder.mapResolvers(replacements));
    }

    @NotNull
    public static List<String> formatListToString(@NotNull List<String> list, TagResolver... resolvers) {
        List<String> newList = new ArrayList<>(list.size());
        for (String line : list) {
            newList.add(formatToString(line, resolvers));
        }

        return newList;
    }

    @NotNull
    public static List<String> formatListToString(@NotNull List<String> list, Map<String, String> replacements) {
        return formatListToString(list, ItemBuilder.mapResolvers(replacements));
    }

    public static String formatNumber(String pattern, double number) {
        DecimalFormat formatter = new DecimalFormat(pattern);
        return formatter.format(number);
    }

    @NotNull
    public static String formatTime(long time) {
        Duration remainingTime = Duration.ofMillis(time);
        long total = remainingTime.getSeconds();
        long hours = total / 3600;
        long minutes = (total % 3600) / 60;
        long seconds = total % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String replaceAll(@NotNull Matcher matcher, @NotNull Function<MatchResult, String> replacer) {
        Objects.requireNonNull(replacer);
        matcher.reset();
        boolean result = matcher.find();
        if (result) {
            StringBuffer sb = new StringBuffer();
            do {
                String replacement = replacer.apply(matcher);
                matcher.appendReplacement(sb, replacement);
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return TEXT.get(matcher);
    }
}
