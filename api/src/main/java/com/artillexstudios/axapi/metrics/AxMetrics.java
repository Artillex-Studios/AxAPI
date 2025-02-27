package com.artillexstudios.axapi.metrics;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.MetricsCollectorRegistry;
import com.artillexstudios.axapi.utils.LogUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class AxMetrics {
    private final MetricsCollectorRegistry registry;
    private final FeatureFlags flags;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private HttpClient client;
    private final long pluginId;
    private ScheduledFuture<?> future;
    private final YamlConfiguration metricsConfig;

    public AxMetrics(AxPlugin plugin, long pluginId) {
        this.pluginId = pluginId;
        this.registry = new MetricsCollectorRegistry(plugin);
        this.flags = plugin.flags();

        Path path = plugin.getDataFolder().toPath();
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

        this.client = HttpClient.newBuilder()
                .build();

        // TODO: figure out how we want to handle threads. 1 thread/plugin seems expensive
        this.future = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::submitData, 60_000, 60_000, TimeUnit.MILLISECONDS);
    }

    public void register(MetricsCollector collector) {
        this.registry.register(collector);
    }

    public void cancel() {
        if (this.future != null && !this.future.isCancelled()) {
            this.future.cancel(false);
        }
    }

    private void submitData() {
        JsonObject obj = new JsonObject();
        obj.addProperty("timestamp", System.currentTimeMillis());
        obj.addProperty("plugin-id", this.pluginId);
        obj.addProperty("port", Bukkit.getPort());

        JsonArray data = new JsonArray();
        for (MetricsCollector collector : this.registry.collectors()) {
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
                if (this.flags.DEBUG.get()) {
                    LogUtils.debug("Sent metrics successfully!");
                }
            }
        } catch (IOException | InterruptedException exception) {
            // Quietly fail
            if (this.flags.DEBUG.get()) {
                LogUtils.debug("Caught exception while sending metrics! Body: {}", serialized, exception);
            }
        }
    }
}
