package com.artillexstudios.axapi.metrics;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.MetricsCollectors;
import com.artillexstudios.axapi.scheduler.ScheduledTask;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.UUID;

public final class AxMetrics {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private final HttpClient client = HttpClient.newBuilder()
            .build();
    private final long pluginId;
    private ScheduledTask task;
    private final YamlConfiguration metricsConfig;

    public AxMetrics(long pluginId) {
        this.pluginId = pluginId;

        Path path = AxPlugin.getProvidingPlugin(AxPlugin.class).getDataFolder().toPath();
        Path metricsConfigPath = path.getParent().resolve("AxAPI").resolve("metrics.yml");
        this.metricsConfig = YamlConfiguration.of(metricsConfigPath, MetricsConfig.class)
                .withDumperOptions(options -> {
                    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                })
                .build();

        this.metricsConfig.load();
    }

    public void start() {
        if (!MetricsConfig.enabled) {
            return;
        }

        this.task = Scheduler.get().runAsyncTimer(this::submitData, 20 * 60, 20 * 60);
    }

    public void cancel() {
        if (this.task != null && !this.task.isCancelled()) {
            this.task.cancel();
        }
    }

    private void submitData() {
        JsonObject obj = new JsonObject();
        obj.addProperty("timestamp", System.currentTimeMillis());
        obj.addProperty("plugin-id", this.pluginId);
        obj.addProperty("port", Bukkit.getPort());
        JsonArray data = new JsonArray();
        for (MetricsCollector collector : MetricsCollectors.collectors()) {
            collector.collect(data);
        }
        obj.add("data", data);
        String serialized = this.gson.toJson(obj);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://metrics.artillex-studios.com/api/v1/upload?uuid=" + MetricsConfig.serverUuid.toString()))
                .POST(HttpRequest.BodyPublishers.ofString(serialized))
                .header("Content-Type", "application/json")
                .build();

        try {
            UUID previousUUID = MetricsConfig.serverUuid;
            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 425) {
                this.metricsConfig.load();
                if (previousUUID.equals(MetricsConfig.serverUuid)) {
                    MetricsConfig.serverUuid = UUID.randomUUID();
                    this.metricsConfig.save();
                }
                LogUtils.warn("Regenerated server uuid, because it was already in use!");
            } else if (response.statusCode() == 200) {
                if (AxPlugin.flags().DEBUG.get()) {
                    LogUtils.debug("Sent metrics successfully!");
                }
            }
        } catch (IOException | InterruptedException exception) {
            // Quietly fail
            if (AxPlugin.flags().DEBUG.get()) {
                LogUtils.debug("Caught exception while sending metrics! Body: {}", serialized, exception);
            }
        }
    }
}
