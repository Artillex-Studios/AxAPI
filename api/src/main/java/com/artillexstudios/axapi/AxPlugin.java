package com.artillexstudios.axapi;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import com.alessiodp.libby.logging.LogLevel;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.gui.AnvilListener;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.placeholders.PlaceholderAPIHook;
import com.artillexstudios.axapi.placeholders.Placeholders;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
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
    private static FeatureFlags flags;

    public AxPlugin() {
        flags = new FeatureFlags(this);
        this.updateFlags(flags);
    }

    public void updateFlags(FeatureFlags flags) {

    }

    @Override
    public void onEnable() {
        if (!NMSHandlers.British.initialise(this)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        DataComponents.setDataComponentImpl(NMSHandlers.getNmsHandler().dataComponents());
        Scheduler.scheduler.init(this);

        if (flags.PACKET_ENTITY_TRACKER_ENABLED.get()) {
            tracker = new EntityTracker();
            tracker.startTicking();
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
                NMSHandlers.getNmsHandler().uninjectPlayer(event.getPlayer());

                if (tracker == null) {
                    return;
                }

                tracker.untrackFor(ServerPlayerWrapper.wrap(event.getPlayer()));
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

                tracker.untrackFor(ServerPlayerWrapper.wrap(event.getPlayer()));
            }
        }, this);
        Bukkit.getPluginManager().registerEvents(new AnvilListener(), this);

        if (flags.HOLOGRAM_UPDATE_TICKS.get() > 0) {
            Holograms.startTicking();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            NMSHandlers.getNmsHandler().injectPlayer(player);
        }

        this.enable();

        Placeholders.lock();
        if (flags.PLACEHOLDER_API_HOOK.get() && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
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

        Library snakeYaml = Library.builder()
                .groupId("org{}yaml")
                .artifactId("snakeyaml")
                .version("2.2")
                .relocate("org{}yaml{}}snakeyaml", "com.artillexstudios.axapi.libs.snakeyaml")
                .build();

        if (flags.DEBUG.get()) {
            libraryManager.setLogLevel(LogLevel.DEBUG);
        }
        libraryManager.loadLibrary(commonsMath);
        libraryManager.loadLibrary(caffeine);
        libraryManager.loadLibrary(snakeYaml);

        this.load();
    }

    public void load() {

    }

    @Override
    public void onDisable() {
        this.disable();
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
        this.reload();

        return System.currentTimeMillis() - start;
    }

    public static FeatureFlags flags() {
        return flags;
    }
}