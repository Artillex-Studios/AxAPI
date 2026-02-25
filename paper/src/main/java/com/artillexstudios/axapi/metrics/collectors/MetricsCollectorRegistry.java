package com.artillexstudios.axapi.metrics.collectors;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.metrics.collectors.implementation.CPUModelMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.JavaVersionMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.MinecraftVersionMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.OnlineModeMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.OnlinePlayersMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.OperatingSystemMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.PluginVersionMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.ServerSoftwareMetricsCollector;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class MetricsCollectorRegistry {
    private final List<MetricsCollector> collectors = new ArrayList<>();

    public MetricsCollectorRegistry(AxPlugin plugin) {
        register(JavaVersionMetricsCollector::new);
        register(MinecraftVersionMetricsCollector::new);
        register(OnlinePlayersMetricsCollector::new);
        register(() -> new PluginVersionMetricsCollector(plugin));
        register(ServerSoftwareMetricsCollector::new);
        register(OnlineModeMetricsCollector::new);

        try {
            Object systemInfo = Class.forName("oshi.SystemInfo").getDeclaredConstructor().newInstance();
            register(() -> new CPUModelMetricsCollector(systemInfo));
            register(() -> new OperatingSystemMetricsCollector(systemInfo));
        } catch (Exception exception) {
            LogUtils.error("Failed to get systeminfo!", exception);
        }
    }

    public void register(Supplier<MetricsCollector> collector) {
        try {
            MetricsCollector metricsCollector = collector.get();
            this.collectors.add(metricsCollector);
        } catch (Exception exception) {
            LogUtils.error("Failed to initialize collector!");
        }
    }

    public List<MetricsCollector> collectors() {
        return Collections.unmodifiableList(this.collectors);
    }
}
