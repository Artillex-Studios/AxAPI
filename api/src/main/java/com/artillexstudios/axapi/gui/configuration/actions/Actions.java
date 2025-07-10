package com.artillexstudios.axapi.gui.configuration.actions;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public enum Actions {
    INSTANCE;

    private final Map<String, Function<String, Action<?>>> registered = new HashMap<>();

    public void register(String id, Function<String, Action<?>> action) {
        this.registered.put(id.toLowerCase(Locale.ENGLISH), action);
    }

    public List<Action<?>> parseAll(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return List.of();
        }

        List<Action<?>> actions = new ArrayList<>(lines.size());
        for (String line : lines) {
            Action<?> parse = this.parse(line);
            if (parse == null) {
                continue;
            }

            actions.add(parse);
        }

        return actions;
    }

    public Action<?> parse(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String id = StringUtils.substringBetween(line, "[", "]").toLowerCase(Locale.ENGLISH);
        Function<String, Action<?>> provider = this.registered.get(id);
        if (provider == null) {
            LogUtils.info("No actionprovider found with id {}!", id);
            return null;
        }

        String arguments = line.substring(id.length()).stripLeading();
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Creating action with id {} with arguments: {}", id, arguments);
        }

        return provider.apply(arguments);
    }
}
