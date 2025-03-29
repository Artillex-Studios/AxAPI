package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MessageUtils {
    private final YamlDocument config;
    private final YamlDocument prefixConfig;
    private final String prefixRoute;

    public MessageUtils(YamlDocument config, String prefixRoute) {
        this.config = config;
        this.prefixConfig = config;
        this.prefixRoute = prefixRoute;
    }

    public MessageUtils(YamlDocument config, String prefixRoute, YamlDocument prefixConfig) {
        this.config = config;
        this.prefixConfig = prefixConfig;
        this.prefixRoute = prefixRoute;
    }

    public static void sendMessage(CommandSender sender, String message, TagResolver... resolvers) {
        sendMessage(sender, "", message, resolvers);
    }

    public static void sendMessage(CommandSender sender, String prefix, String message, TagResolver... resolvers) {
        if (message.isEmpty()) {
            return;
        }

        if (sender instanceof Player player) {
            ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
            wrapper.message(StringUtils.format(prefix + message, resolvers));
        } else {
            sender.sendMessage(StringUtils.formatToString(prefix + message, resolvers));
        }
    }

    public void sendFormatted(CommandSender sender, String message, TagResolver... resolvers) {
        if (message.isEmpty()) {
            return;
        }

        if (sender instanceof Player player) {
            ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
            wrapper.message(StringUtils.format(message, resolvers));
        } else {
            sender.sendMessage(StringUtils.formatToString(message, resolvers));
        }
    }

    public void sendLang(CommandSender commandSender, String message, TagResolver... resolvers) {
        String configString = config.getString(message);
        if (configString.isEmpty()) {
            return;
        }

        sendFormatted(commandSender, prefixConfig.getString(prefixRoute) + configString, resolvers);
    }

    public void sendFormatted(CommandSender sender, String message, Map<String, String> replacements) {
        AtomicReference<String> toFormat = new AtomicReference<>(message);
        replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));

        sendFormatted(sender, toFormat.get());
    }

    public void sendLang(CommandSender sender, String message, Map<String, String> replacements) {
        String parsed = config.getString(message);
        if (parsed.isEmpty()) {
            return;
        }

        sendFormatted(sender, prefixConfig.getString(prefixRoute) + parsed, replacements);
    }
}
