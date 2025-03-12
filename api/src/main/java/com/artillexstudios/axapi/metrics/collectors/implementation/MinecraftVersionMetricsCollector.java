package com.artillexstudios.axapi.metrics.collectors.implementation;

import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MinecraftVersionMetricsCollector implements MetricsCollector {
    private static final Pattern pattern = Pattern.compile("MC: (\\d+\\.\\d+(\\.\\d+))?");
    private final String version;

    public MinecraftVersionMetricsCollector() {
        Matcher matcher = pattern.matcher(Bukkit.getVersion());
        matcher.find();
        this.version = matcher.group(1);
    }

    @Override
    public void collect(JsonArray data) {
        JsonObject object = new JsonObject();
        object.addProperty("@type", "minecraft-version");

        object.addProperty("minecraft-version", this.version);
        data.add(object);
    }
}
