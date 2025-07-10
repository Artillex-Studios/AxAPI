package com.artillexstudios.axapi.gui.configuration.actions;

import com.artillexstudios.axapi.gui.configuration.actions.implementation.CloseAction;
import com.artillexstudios.axapi.gui.configuration.actions.implementation.ConsoleCommandAction;
import com.artillexstudios.axapi.gui.configuration.actions.implementation.MessageAction;
import com.artillexstudios.axapi.gui.configuration.actions.implementation.PageChangeAction;
import com.artillexstudios.axapi.gui.configuration.actions.implementation.PlayerCommandAction;
import com.artillexstudios.axapi.gui.configuration.actions.implementation.RefreshAction;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public enum Actions {
    INSTANCE;


    private final Map<String, ActionProvider<?>> registered;

    {
        this.registered = new HashMap<>();
        this.register("close", CloseAction::new);
        this.register("console", ConsoleCommandAction::new);
        this.register("message", MessageAction::new);
        this.register("page", PageChangeAction::new);
        this.register("player", PlayerCommandAction::new);
        this.register("refresh", RefreshAction::new);
    }


    public void register(String id, ActionProvider<?> action) {
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
        ActionProvider<?> provider = this.registered.get(id);
        if (provider == null) {
            LogUtils.info("No actionprovider found with id {}!", id);
            return null;
        }

        String arguments = line.substring(id.length() + 2).strip();
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Creating action with id: {} with arguments: {}", id, arguments);
        }

        try {
            return provider.provide(arguments);
        } catch (IllegalArgumentException exception) {
            LogUtils.error("Failed to construct action with id: {} and arguments: {}!", id, arguments);
            return null;
        }
    }
}
