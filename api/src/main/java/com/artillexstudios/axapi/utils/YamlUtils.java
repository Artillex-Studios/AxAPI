package com.artillexstudios.axapi.utils;

import me.neznamy.yamlassist.YamlAssist;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class YamlUtils {

    public static boolean suggest(@NotNull File file) {
        List<String> suggestions = YamlAssist.getSuggestions(file);
        if (suggestions.isEmpty()) return true;

        // TODO: Log suggestions
        for (String suggestion : suggestions) {

        }
        return false;
    }
}
