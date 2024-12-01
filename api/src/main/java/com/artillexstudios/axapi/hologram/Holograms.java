package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.utils.ExceptionReportingScheduledThreadPool;
import com.artillexstudios.axapi.utils.LogUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class Holograms {
    private static final JavaPlugin plugin = AxPlugin.getPlugin(AxPlugin.class);
    private static final Int2ObjectLinkedOpenHashMap<HologramLine> linesMap = new Int2ObjectLinkedOpenHashMap<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static ScheduledExecutorService hologramUpdater;

    public static void startTicking() {
        shutdown();
        hologramUpdater = new ExceptionReportingScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat(plugin.getName() + "-Hologram-Update-Thread").build());
        hologramUpdater.scheduleAtFixedRate(() -> {
            Holograms.getMap(map -> {
                map.forEach((id, line) -> {
                    if (!line.hasPlaceholders()) return;

                    line.update();
                });
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

    public static void put(int entityId, HologramLine line) {
        lock.writeLock().lock();
        try {
            linesMap.put(entityId, line);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void remove(int entityId) {
        lock.writeLock().lock();
        try {
            linesMap.remove(entityId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static HologramLine byId(int entityId) {
        lock.readLock().lock();
        try {
            return linesMap.get(entityId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void getMap(Consumer<Int2ObjectLinkedOpenHashMap<HologramLine>> map) {
        lock.readLock().lock();
        try {
            map.accept(linesMap);
        } finally {
            lock.readLock().unlock();
        }
    }
}
