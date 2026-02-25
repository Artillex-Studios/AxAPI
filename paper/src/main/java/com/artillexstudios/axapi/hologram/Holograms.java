package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.executor.ExceptionReportingScheduledThreadPool;
import com.artillexstudios.axapi.hologram.page.HologramPage;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Holograms {
    private static final AxPlugin plugin = AxPlugin.getPlugin(AxPlugin.class);
    private static final ConcurrentHashMap<Integer, HologramPage<?, ?>> pagesMap = new ConcurrentHashMap<>();
    private static ScheduledExecutorService hologramUpdater;

    public static void startTicking() {
        shutdown();
        hologramUpdater = new ExceptionReportingScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat(plugin.getName() + "-Hologram-Update-Thread").build());
        hologramUpdater.scheduleAtFixedRate(() -> {
            Holograms.getView().forEach((id, page) -> {
                if (!page.containsPlaceholders()) {
                    return;
                }

                page.update();
            });
        }, 0, FeatureFlags.HOLOGRAM_UPDATE_TICKS.get() * 50, TimeUnit.MILLISECONDS);
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

    public static void put(int entityId, HologramPage<?, ?> page) {
        pagesMap.put(entityId, page);
    }

    public static void remove(int entityId) {
        pagesMap.remove(entityId);
    }

    public static HologramPage<?, ?> byId(int entityId) {
        return pagesMap.get(entityId);
    }

    public static Map<Integer, HologramPage<?, ?>> getView() {
        return Collections.unmodifiableMap(pagesMap);
    }
}
