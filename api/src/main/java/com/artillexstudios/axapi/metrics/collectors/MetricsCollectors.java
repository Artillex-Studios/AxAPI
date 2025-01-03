package com.artillexstudios.axapi.metrics.collectors;

import com.artillexstudios.axapi.metrics.collectors.implementation.CPUModelMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.JavaVersionMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.MinecraftVersionMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.OnlineModeMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.OnlinePlayersMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.OperatingSystemMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.PluginVersionMetricsCollector;
import com.artillexstudios.axapi.metrics.collectors.implementation.ServerSoftwareMetricsCollector;
import com.artillexstudios.axapi.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MetricsCollectors {
    private static final List<MetricsCollector> collectors = new ArrayList<>();

    static {
        register(new JavaVersionMetricsCollector());
        register(new MinecraftVersionMetricsCollector());
        register(new OnlinePlayersMetricsCollector());
        register(new PluginVersionMetricsCollector());
        register(new ServerSoftwareMetricsCollector());
        register(new OnlineModeMetricsCollector());

        try {
            Object systemInfo = Class.forName("oshi.SystemInfo").getDeclaredConstructor().newInstance();
            register(new CPUModelMetricsCollector(systemInfo));
            register(new OperatingSystemMetricsCollector(systemInfo));
        } catch (Exception exception) {
            LogUtils.error("Failed to get systeminfo!", exception);
        }
    }

    public static List<MetricsCollector> collectors() {
        return Collections.unmodifiableList(collectors);
    }

    public static void register(MetricsCollector collector) {
        collectors.add(collector);
    }
}
