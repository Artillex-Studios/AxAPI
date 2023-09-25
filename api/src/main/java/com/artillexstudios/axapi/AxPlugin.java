package com.artillexstudios.axapi;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pluginlib.DependentJavaPlugin;
import pluginlib.PluginLib;
import pluginlib.Relocation;

public abstract class AxPlugin extends DependentJavaPlugin {

    private static final PluginLib ADVENTURE_BUKKIT = PluginLib.builder()
            .mavenCentral()
            .groupId("net.kyori")
            .artifactId("adventure-platform-bukkit")
            .version("4.3.0")
            .relocate(new Relocation("net/kyori", "com.artillexstudios.axapi.libs.kyori"))
            .build();

    private static final PluginLib MINI_MESSAGE = PluginLib.builder()
            .mavenCentral()
            .groupId("net.kyori")
            .artifactId("adventure-text-minimessage")
            .version("4.14.0")
            .relocate(new Relocation("net/kyori", "com.artillexstudios.axapi.libs.kyori"))
            .build();

    private static final PluginLib FAST_UTIL = PluginLib.builder()
            .mavenCentral()
            .groupId("it.unimi.dsi")
            .artifactId("fastutil")
            .version("8.5.12")
            .relocate(new Relocation("it/unimi/dsi", "com.artillexstudios.axapi.libs.fastutil"))
            .build();

    static {
        ADVENTURE_BUKKIT.load(AxPlugin.class);
        MINI_MESSAGE.load(AxPlugin.class);
        FAST_UTIL.load(AxPlugin.class);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
                PacketEntityTracker.clearPlayer(event.getPlayer());
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
        Scheduler.scheduler.init(this);
        NMSHandlers.initialise();
        Scheduler.get().runAsyncTimer(task -> PacketEntityTracker.tickAll(), 0, 1);

        load();
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