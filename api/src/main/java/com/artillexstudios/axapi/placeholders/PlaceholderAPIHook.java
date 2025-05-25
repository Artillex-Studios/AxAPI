package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    private final AxPlugin plugin;

    public PlaceholderAPIHook(AxPlugin plugin) {
        this.plugin = plugin;
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
        if (this.plugin.flags().DEBUG.get()) {
            LogUtils.debug("Requesting placeholders for {} user, with parameters: {}!", player, params);
        }

        return PlaceholderHandler.parse("%" + params + "%", player);
    }

    @Nullable
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (this.plugin.flags().DEBUG.get()) {
            LogUtils.debug("Requesting placeholders for {} user, with parameters: {}!", player, params);
        }

        return PlaceholderHandler.parse("%" + params + "%", player);
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return PlaceholderHandler.placeholders(this.plugin.flags().PLACEHOLDER_API_IDENTIFIER.get());
    }
}
