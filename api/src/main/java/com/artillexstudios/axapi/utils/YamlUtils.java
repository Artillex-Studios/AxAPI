package com.artillexstudios.axapi.utils;

import me.neznamy.yamlassist.YamlAssist;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class YamlUtils {

    public static boolean suggest(@NotNull File file) {
        List<String> suggestions = YamlAssist.getSuggestions(file);
        if (suggestions.isEmpty()) return true;

        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("<color:#ff0000>Possible solutions:"));
        for (String suggestion : suggestions) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("<color:#ff0000> - <white>" + suggestion));
        }
        return false;
    }
}
