package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.AxPlugin;
import me.neznamy.yamlassist.YamlAssist;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class YamlUtils {
    private static final Plugin INSTANCE = AxPlugin.getPlugin(AxPlugin.class);

    public static boolean suggest(@NotNull File file) {
        List<String> suggestions = YamlAssist.getSuggestions(file);
        if (suggestions.isEmpty()) return true;

        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("[<plugin>] <color:#ff0000>There were issues while reloading <white><file></white> file!", Placeholder.parsed("plugin", INSTANCE.getDescription().getName()), Placeholder.parsed("file", file.getName())));
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("<color:#ff0000>Possible solutions:"));
        for (String suggestion : suggestions) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("<color:#ff0000> - <white>" + suggestion));
        }

        return false;
    }
}
