package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.AxPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class PlaceholderAPIHook extends PlaceholderExpansion {
    private static final JavaPlugin plugin = AxPlugin.getPlugin(AxPlugin.class);

    @Override
    public boolean persist() {
        return true;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "axteams";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "Artillex-Studios";
    }

    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
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
