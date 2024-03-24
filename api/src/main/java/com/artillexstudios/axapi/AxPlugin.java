package com.artillexstudios.axapi;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.FeatureFlags;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AxPlugin extends JavaPlugin {
    private static final Logger log = LoggerFactory.getLogger(AxPlugin.class);
    public static PacketEntityTracker tracker;
    private static boolean hasNMSHandler;

    public AxPlugin() {
        updateFlags();
    }

    public void updateFlags() {

    }

    @Override
    public void onEnable() {
        Scheduler.scheduler.init(this);

        if (hasNMSHandler) {
            if (tracker != null) {
                Executors.newScheduledThreadPool(FeatureFlags.PACKET_ENTITY_TRACKER_THREADS.get()).scheduleAtFixedRate(() -> {
                    try {
                        tracker.process();
                    } catch (Exception exception) {
                        log.error("An unexpected error occurred while processing packet entities via the tracker!", exception);
                    }
                }, 0, 50, TimeUnit.MILLISECONDS);
            }

            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
                    NMSHandlers.getNmsHandler().uninjectPlayer(event.getPlayer());
                }

                @EventHandler
                public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
                    NMSHandlers.getNmsHandler().injectPlayer(event.getPlayer());
                }

                @EventHandler
                public void onPlayerChangedWorldEvent(@NotNull final PlayerChangedWorldEvent event) {
                    if (tracker == null) {
                        return;
                    }

                    tracker.untrackFor(event.getPlayer());
                }
            }, this);

            if (FeatureFlags.HOLOGRAM_UPDATE_TICKS.get() > 0) {
                Scheduler.get().runAsyncTimer(scheduledTask -> {
                    Holograms.getMap(map -> {
                        map.forEach((id, line) -> {
                            if (!line.containsPlaceholders()) return;

                            line.getEntity().sendMetaUpdate();
                        });
                    });
                }, 0L, FeatureFlags.HOLOGRAM_UPDATE_TICKS.get());
            }
        }

        enable();
    }

    public void enable() {

    }

    @Override
    public void onLoad() {
        BukkitLibraryManager libraryManager = new BukkitLibraryManager(this);
        libraryManager.addMavenCentral();

        Library commonsMath = Library.builder()
                .groupId("org{}apache{}commons")
                .artifactId("commons-math3")
                .version("3.6.1")
                .build();

        Library caffeine = Library.builder()
                .groupId("com{}github{}ben-manes{}caffeine")
                .artifactId("caffeine")
                .version("2.9.2")
                .build();

        libraryManager.loadLibrary(commonsMath);
        libraryManager.loadLibrary(caffeine);

        hasNMSHandler = NMSHandlers.British.initialise(this);

        load();
        if (hasNMSHandler && FeatureFlags.PACKET_ENTITY_TRACKER_ENABLED.get()) {
            tracker = NMSHandlers.getNmsHandler().newTracker();
        }
    }

    public void load() {

    }

    @Override
    public void onDisable() {
        disable();
        Scheduler.get().cancelAll();
    }

    public void disable() {

    }

    public void reload() {

    }

    public long reloadWithTime() {
        long start = System.currentTimeMillis();
        reload();

        return System.currentTimeMillis() - start;
    }
}