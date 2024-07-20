package com.artillexstudios.axapi.utils;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyTranslator {
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([a-fA-F0-9]{6})>|<color:#([a-fA-F0-9])>|<c:#([a-fA-F0-9])>");
    private static final Pattern HEX_END_PATTERN = Pattern.compile("</[^/>]+>");
    private static final ObjectImmutableList<Pair<String, String>> minimessage = ObjectImmutableList.of(
            Pair.of("<black>", "§0"),
            Pair.of("<dark_blue>", "§1"),
            Pair.of("<dark_green>", "§2"),
            Pair.of("<dark_aqua>", "§3"),
            Pair.of("<dark_red>", "§4"),
            Pair.of("<dark_purple>", "§5"),
            Pair.of("<gold>", "§6"),
            Pair.of("<gray>", "§7"),
            Pair.of("<dark_gray>", "§8"),
            Pair.of("<blue>", "§9"),
            Pair.of("<green>", "§a"),
            Pair.of("<aqua>", "§b"),
            Pair.of("<red>", "§c"),
            Pair.of("<light_purple>", "§d"),
            Pair.of("<yellow>", "§e"),
            Pair.of("<white>", "§f"),
            Pair.of("<reset>", "§r"),
            Pair.of("<br>", "\n"),
            Pair.of("<b>", "§l"),
            Pair.of("<bold>", "§l"),
            Pair.of("</b>", "§r"),
            Pair.of("</bold>", "§r"),
            Pair.of("<obf>", "§k"),
            Pair.of("</obf>", "§r"),
            Pair.of("<obfuscated>", "§k"),
            Pair.of("</obfuscated>", "§r"),
            Pair.of("<st>", "§m"),
            Pair.of("</st>", "§r"),
            Pair.of("<strikethrough>", "§m"),
            Pair.of("</strikethrough>", "§r"),
            Pair.of("<u>", "§n"),
            Pair.of("</u>", "§r"),
            Pair.of("<underlined>", "§n"),
            Pair.of("</underlined>", "§r"),
            Pair.of("<i>", "§o"),
            Pair.of("</i>", "§r"),
            Pair.of("<italic>", "§o"),
            Pair.of("</italic>", "§r")
    );

    public static String flatten(String string) {
        String toFormat = string;

        for (Pair<String, String> stringStringPair : minimessage) {
            toFormat = toFormat.replace(stringStringPair.getKey(), stringStringPair.getValue());
        }

        Matcher matcher = HEX_PATTERN.matcher(toFormat);
        toFormat = StringUtils.replaceAll(matcher, a -> {
            String group = a.group(1);
            return "&x&" +
                    group.charAt(0) + "&" +
                    group.charAt(1) + "&" +
                    group.charAt(2) + "&" +
                    group.charAt(3) + "&" +
                    group.charAt(4) + "&" +
                    group.charAt(5);
        });

        toFormat = HEX_END_PATTERN.matcher(toFormat).replaceAll("");

        return ChatColor.translateAlternateColorCodes('&', toFormat);
    }

    public static String flatten(Component component) {
        return flatten(MiniMessage.miniMessage().serialize(component));
    }
}
