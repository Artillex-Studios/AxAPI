package com.artillexstudios.axapi.metrics.collectors.implementation;

import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftVersionMetricsCollector implements MetricsCollector {
    private static final Pattern pattern = Pattern.compile("MC: (\\d+\\.\\d+\\.\\d+)");

    @Override
    public void collect(JsonArray data) {
        JsonObject object = new JsonObject();
        object.addProperty("@type", "minecraft-version");
        Matcher matcher = pattern.matcher(Bukkit.getVersion());
        matcher.find();
        object.addProperty("minecraft-version", matcher.group(1));
        data.add(object);
    }
}
