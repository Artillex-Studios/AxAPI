package com.artillexstudios.axapi.metrics.collectors.implementation;

import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MinecraftVersionMetricsCollector implements MetricsCollector {
    private static final Pattern pattern = Pattern.compile("MC: (\\d+\\.\\d+(\\.\\d+)?)");
    private final String version;

    public MinecraftVersionMetricsCollector() {
        Matcher matcher = pattern.matcher(Bukkit.getVersion());
        if (matcher.find()) {
            this.version = matcher.group(1);
        } else {
            LogUtils.error("Failed to get version from Bukkit.getVersion(), didn't match pattern! Was: {}", Bukkit.getVersion());
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void collect(JsonArray data) {
        JsonObject object = new JsonObject();
        object.addProperty("@type", "minecraft-version");

        object.addProperty("minecraft-version", this.version);
        data.add(object);
    }
}
