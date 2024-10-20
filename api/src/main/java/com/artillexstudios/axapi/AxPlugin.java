package com.artillexstudios.axapi;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import com.alessiodp.libby.logging.LogLevel;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.placeholders.PlaceholderAPIHook;
import com.artillexstudios.axapi.placeholders.Placeholders;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.FeatureFlags;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AxPlugin extends JavaPlugin {
    public static EntityTracker tracker;
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
                tracker.startTicking();
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
                public void onPacketEntityInteractEvent(@NotNull final PacketEntityInteractEvent event) {
                    event.getPacketEntity().callInteract(event);
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
                Holograms.startTicking();
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            NMSHandlers.getNmsHandler().injectPlayer(player);
        }

        enable();

        Placeholders.lock();
        if (FeatureFlags.PLACEHOLDER_API_HOOK.get() && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook().register();
        }
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
                .relocate("org{}apache{}commons{}math3", "com.artillexstudios.axapi.libs.math3")
                .build();

        Library caffeine = Library.builder()
                .groupId("com{}github{}ben-manes{}caffeine")
                .artifactId("caffeine")
                .version("3.1.8")
                .relocate("com{}github{}benmanes", "com.artillexstudios.axapi.libs.caffeine")
                .build();

        libraryManager.setLogLevel(LogLevel.DEBUG);
        libraryManager.loadLibrary(commonsMath);
        libraryManager.loadLibrary(caffeine);

        hasNMSHandler = NMSHandlers.British.initialise(this);
        DataComponents.setDataComponentImpl(NMSHandlers.getNmsHandler().dataComponents());

        load();
        if (hasNMSHandler && FeatureFlags.PACKET_ENTITY_TRACKER_ENABLED.get()) {
            tracker = new EntityTracker();
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

        if (tracker != null) {
            tracker.shutdown();
        }
        Holograms.shutdown();
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