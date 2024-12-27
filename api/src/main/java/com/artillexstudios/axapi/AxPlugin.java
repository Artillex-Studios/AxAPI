package com.artillexstudios.axapi;

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
import com.artillexstudios.axapi.utils.LogUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public abstract class AxPlugin {
    private static JavaPlugin plugin;
    public static EntityTracker tracker;
    private static FeatureFlags flags;

    public AxPlugin(JavaPlugin plugin) {
        LogUtils.debug("Loaded class from classloader: {}!", this.getClass().getClassLoader());
        AxPlugin.plugin = plugin;
        flags = new FeatureFlags(this);
        this.updateFlags(flags);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public void updateFlags(FeatureFlags flags) {

    }

    public InputStream getResource(String fileName) {
        return plugin.getResource(fileName);
    }

    public PluginDescriptionFile getDescription() {
        return plugin.getDescription();
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public String getName() {

        return plugin.getName();
    }

    public void onEnable() {
        if (!NMSHandlers.British.initialise(plugin)) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        DataComponents.setDataComponentImpl(NMSHandlers.getNmsHandler().dataComponents());
        Scheduler.scheduler.init(plugin);

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
        }, plugin);
        Bukkit.getPluginManager().registerEvents(new AnvilListener(), plugin);

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

    public void onLoad() {
        this.load();
    }

    public void load() {

    }

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