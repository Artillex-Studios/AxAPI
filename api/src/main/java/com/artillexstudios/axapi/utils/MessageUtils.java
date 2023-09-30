package com.artillexstudios.axapi.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MessageUtils {
    private final YamlDocument config;
    private final String prefixRoute;

    public MessageUtils(YamlDocument config, String prefixRoute) {
        this.config = config;
        this.prefixRoute = prefixRoute;
    }

    public void sendFormatted(CommandSender sender, String message, TagResolver... resolvers) {
        sender.sendMessage(StringUtils.formatToString(message, resolvers));
    }

    public void sendLang(CommandSender commandSender, String message, TagResolver... resolvers) {
        sendFormatted(commandSender, config.getString(prefixRoute) + config.getString(message), resolvers);
    }

    public void sendFormatted(CommandSender sender, String message, Map<String, String> replacements) {
        AtomicReference<String> toFormat = new AtomicReference<>(message);
        replacements.forEach((pattern, replacement) -> toFormat.set(toFormat.get().replace(pattern, replacement)));

        sendFormatted(sender, toFormat.get());
    }

    public void sendLang(CommandSender sender, String message, Map<String, String> replacements) {
        sendFormatted(sender, config.getString(prefixRoute) + config.getString(message), replacements);
    }
}
