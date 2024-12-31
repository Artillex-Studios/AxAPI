package com.artillexstudios.axapi.metrics.collectors.implementation;

import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

public class ServerSoftwareMetricsCollector implements MetricsCollector {

    @Override
    public void collect(JsonArray data) {
        JsonObject object = new JsonObject();
        object.addProperty("@type", "server-software");
        object.addProperty("server-software", Bukkit.getName());
        data.add(object);
    }
}
