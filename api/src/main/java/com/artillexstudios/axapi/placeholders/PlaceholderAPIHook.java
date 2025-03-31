package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.placeholders.refactor.PlaceholderParser;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public final class PlaceholderAPIHook extends PlaceholderExpansion {
    private final PlaceholderParser parser;
    private final AxPlugin plugin;

    public PlaceholderAPIHook(PlaceholderParser parser, AxPlugin plugin) {
        this.parser = parser;
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        String identifier = this.plugin.flags().PLACEHOLDER_API_IDENTIFIER.get();
        if (identifier.isBlank()) {
            String pluginName = this.plugin.getName().toLowerCase(Locale.ENGLISH);
            LogUtils.error("PlaceholderAPI identifier is not set up! Please set it! Defaulting to {}", pluginName);
            return pluginName;
        }

        return identifier;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "Artillex-Studios";
    }

    @NotNull
    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Nullable
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return Placeholders.parse(params, Context.builder(ParseContext.PLACEHOLDER_API, ResolutionType.ONLINE)
                .add(Player.class, player)
        );
    }

    @Nullable
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        return Placeholders.parse(params, Context.builder(ParseContext.PLACEHOLDER_API, ResolutionType.OFFLINE)
                .add(OfflinePlayer.class, player)
        );
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return Placeholders.placeholders(ParseContext.PLACEHOLDER_API);
    }
}
