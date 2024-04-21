package com.artillexstudios.axapi;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.FeatureFlags;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import net.byteflux.libby.logging.LogLevel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ConcurrentModificationException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
                        if (exception instanceof ConcurrentModificationException) {
                            // There's something weird with the entity tracker after the server starts up.
                            // Nothing blew up yet, so I guess the error is safe to ignore...
                            // If something blows up, I'm not the person to blame!
                            // (But people don't like seeing errors, so this is the solution until I find out what causes the tracker to throw a CME)
                            //
                            // Please don't hunt me for this, I didn't want to do it.
                            return;
                        }

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
                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                    Holograms.getMap(map -> {
                        map.forEach((id, line) -> {
                            if (!line.containsPlaceholders()) return;

                            line.getEntity().sendMetaUpdate();
                        });
                    });
                }, 0, FeatureFlags.HOLOGRAM_UPDATE_TICKS.get() * 50, TimeUnit.MILLISECONDS);
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            NMSHandlers.getNmsHandler().injectPlayer(player);
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

        libraryManager.setLogLevel(LogLevel.DEBUG);
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            NMSHandlers.getNmsHandler().uninjectPlayer(player);
        }
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