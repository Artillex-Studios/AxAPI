package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.PlaceholderParameterNotInContextException;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.Optionals;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PaperPlaceholderHandler extends PlaceholderHandler {

    public static String parseWithPlaceholderAPI(String line, PlaceholderParameters parameters) {
        String newLine = parse(line, parameters);
        if (ClassUtils.INSTANCE.classExists("me.clip.placeholderapi.PlaceholderAPI")) {
            try {
                newLine = PlaceholderAPI.setPlaceholders(Optionals.orElse(parameters.getByName("player"), parameters.resolve(Player.class)), newLine);
            } catch (PlaceholderParameterNotInContextException exception) {
                return newLine;
            }
        }

        return newLine;
    }
}
