package com.artillexstudios.axapi.metrics.collectors.implementation;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PluginVersionMetricsCollector implements MetricsCollector {

    @Override
    public void collect(JsonArray data) {
        JsonObject object = new JsonObject();
        object.addProperty("@type", "plugin-version");
        object.addProperty("plugin-version", AxPlugin.getPlugin(AxPlugin.class).getDescription().getVersion());
        data.add(object);
    }
}
