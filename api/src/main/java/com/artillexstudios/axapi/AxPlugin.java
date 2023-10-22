package com.artillexstudios.axapi;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AxPlugin extends JavaPlugin {
    public static PacketEntityTracker tracker;

    @Override
    public void onEnable() {
        Scheduler.scheduler.init(this);

        Executors.newScheduledThreadPool(3).scheduleAtFixedRate(() -> {
            try {
                tracker.process();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, 0, 50, TimeUnit.MILLISECONDS);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
                NMSHandlers.getNmsHandler().uninjectPlayer(event.getPlayer());
            }

            @EventHandler
            public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
                NMSHandlers.getNmsHandler().injectPlayer(event.getPlayer());
            }
        }, this);

        enable();
    }

    public void enable() {

    }

    @Override
    public void onLoad() {
        NMSHandlers.initialise(this);

        load();
        tracker = NMSHandlers.getNmsHandler().newTracker();
    }

    public void load() {

    }

    @Override
    public void onDisable() {
        disable();
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