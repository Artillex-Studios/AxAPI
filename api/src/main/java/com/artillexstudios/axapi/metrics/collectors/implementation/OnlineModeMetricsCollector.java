package com.artillexstudios.axapi.metrics.collectors.implementation;

import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

public class OnlineModeMetricsCollector implements MetricsCollector {
    private final boolean onlineMode = Bukkit.getOnlineMode();

    @Override
    public void collect(JsonArray data) {
        JsonObject object = new JsonObject();
        object.addProperty("@type", "online-mode");
        object.addProperty("online-mode", this.onlineMode);
        data.add(object);
    }
}
