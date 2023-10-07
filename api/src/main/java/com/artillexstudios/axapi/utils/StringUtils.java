package com.artillexstudios.axapi.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER;

    public static final MiniMessage MINI_MESSAGE;

    static {
        if (Version.getServerVersion().protocolId >= Version.v1_16_5.protocolId) {
            MINI_MESSAGE = MiniMessage.builder()
                    .tags(StandardTags.defaults())
                    .build();

            LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();
        } else {
            MINI_MESSAGE = MiniMessage.builder()
                    .tags(StandardTags.defaults())
                    .build();

            LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.legacyAmpersand()
                    .toBuilder()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();
        }
    }

    @NotNull
    public static Component format(@NotNull String input, TagResolver... resolvers) {
        return MINI_MESSAGE.deserialize(input, resolvers).applyFallbackStyle(TextDecoration.ITALIC.withState(false));
    }

    @NotNull
    public static String formatToString(@NotNull String input, TagResolver... resolvers) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(format(input, resolvers));
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
    public static List<String> formatListToString(@NotNull List<String> list, TagResolver... resolvers) {
        List<String> newList = new ArrayList<>(list.size());
        for (String line : list) {
            newList.add(formatToString(line, resolvers));
        }

        return newList;
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
}
