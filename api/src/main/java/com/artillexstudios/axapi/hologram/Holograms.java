package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.executor.ExceptionReportingScheduledThreadPool;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Holograms {
    private static final AxPlugin plugin = AxPlugin.getPlugin(AxPlugin.class);
    private static final ConcurrentHashMap<Integer, HologramLine> linesMap = new ConcurrentHashMap<>();
    private static ScheduledExecutorService hologramUpdater;

    public static void startTicking() {
        shutdown();
        hologramUpdater = new ExceptionReportingScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat(plugin.getName() + "-Hologram-Update-Thread").build());
        hologramUpdater.scheduleAtFixedRate(() -> {
            Holograms.getMap(map -> {
                map.forEach((id, line) -> {
                    if (!line.hasPlaceholders()) {
                        return;
                    }

                    line.update();
                });
            });
        }, 0, plugin.flags().HOLOGRAM_UPDATE_TICKS.get() * 50, TimeUnit.MILLISECONDS);
    }

    public static void shutdown() {
        if (hologramUpdater != null) {
            hologramUpdater.shutdown();
            try {
                hologramUpdater.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                LogUtils.error("An unexpected error occurred while disabling hologram ticker!", exception);
            }
        }
    }

    public static void put(int entityId, HologramLine line) {
        linesMap.put(entityId, line);
    }

    public static void remove(int entityId) {
        linesMap.remove(entityId);
    }

    public static HologramLine byId(int entityId) {
        return linesMap.get(entityId);
    }

    public static void getMap(Consumer<ConcurrentHashMap<Integer, HologramLine>> map) {
        map.accept(linesMap);
    }
}
