package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.unimi.dsi.fastutil.chars.CharImmutableList;
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
    private static final AxPlugin plugin = AxPlugin.getPlugin(AxPlugin.class);
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");
    private static final Pattern UNUSUAL_LEGACY_HEX_PATTERN = Pattern.compile("&x&([a-fA-F0-9])&([a-fA-F0-9])&([a-fA-F0-9])&([a-fA-F0-9])&([a-fA-F0-9])&([a-fA-F0-9])");
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
            Pair.of("&r", "<reset>"),
            Pair.of("\n", "<br>")
    );
    private static final CharImmutableList COLOR_CHARS = CharImmutableList.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', '#');
    private static final Cache<String, String> COLOR_CACHE = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(1))
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
        if (plugin.flags().USE_LEGACY_HEX_FORMATTER.get()) {
            input = ItemBuilder.toTagResolver(input, resolvers);

            return LEGACY_COMPONENT_SERIALIZER.deserialize(formatToString(input, resolvers)).applyFallbackStyle(TextDecoration.ITALIC.withState(false));
        }

        // I will probably have to improve the performance of this code by a large margin...
        String formatted = COLOR_CACHE.get(input, str -> {
            String toFormat = str.replace('\u00a7', '&');

            toFormat = replaceLegacyFormat(toFormat, "&l", "<b>", "</b>");
            toFormat = replaceLegacyFormat(toFormat, "&m", "<st>", "</st>");
            toFormat = replaceLegacyFormat(toFormat, "&n", "<u>", "</u>");
            toFormat = replaceLegacyFormat(toFormat, "&o", "<i>", "</i>");
            toFormat = replaceLegacyFormat(toFormat, "&k", "<obf>", "</obf>");

            toFormat = replaceAll(HEX_PATTERN.matcher(toFormat), fo -> "<#" + fo.group(1) + ">");
            toFormat = replaceAll(UNUSUAL_LEGACY_HEX_PATTERN.matcher(toFormat), fo -> "<#" + fo.group(0) + fo.group(1) + fo.group(2) + fo.group(3) + fo.group(4) + fo.group(5) + ">");
            toFormat = replaceAll(UNUSUAL_LEGACY_HEX_PATTERN.matcher(toFormat), fo -> "");

            for (Pair<String, String> placeholder : COLOR_FORMATS) {
                toFormat = toFormat.replace(placeholder.first(), placeholder.second());
            }

            toFormat = ItemBuilder.toTagResolver(toFormat, resolvers);

            return toFormat;
        });

        if (formatted == null) {
            return Component.empty();
        }

        return MINI_MESSAGE.deserialize(formatted, resolvers).applyFallbackStyle(TextDecoration.ITALIC.withState(false));
    }

    public static String formatToString(@NotNull String string, @NotNull TagResolver... resolvers) {
        if (plugin.flags().USE_LEGACY_HEX_FORMATTER.get()) {
            String changed = string.replace("§", "&");
            changed = ItemBuilder.toTagResolver(changed, resolvers);
            return ChatColor.translateAlternateColorCodes('&', legacyHexFormat(LEGACY_COMPONENT_SERIALIZER.serialize(MINI_MESSAGE.deserialize(changed, resolvers))));
        }

        return LEGACY_COMPONENT_SERIALIZER.serialize(format(string, resolvers));
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

    private static String replaceLegacyFormat(String toFormat, String search, String start, String close) {
        int index;
        while ((index = toFormat.indexOf(search)) != -1) {
            toFormat = org.apache.commons.lang.StringUtils.replaceOnce(toFormat, search, start);
            for (int i = index; i < toFormat.length(); i++) {
                if (toFormat.charAt(i) == '&' && i + 1 < toFormat.length() && COLOR_CHARS.contains(toFormat.charAt(i + 1))) {
                    StringBuilder stringBuilder = new StringBuilder(toFormat);
                    stringBuilder.insert(i, close);
                    toFormat = stringBuilder.toString();
                    break;
                }
            }
        }

        return toFormat;
    }

    // Thanks! https://www.spigotmc.org/threads/hex-color-code-translate.449748/
    public static String legacyHexFormat(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder builder = new StringBuilder(message.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(builder, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1)
                    + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3)
                    + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5)
            );
        }

        return matcher.appendTail(builder).toString();
    }

    public static String replaceAll(@NotNull Matcher matcher, @NotNull Function<MatchResult, String> replacer) {
        Objects.requireNonNull(replacer);
        boolean result = matcher.find();
        if (result) {
            StringBuilder sb = new StringBuilder();
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
