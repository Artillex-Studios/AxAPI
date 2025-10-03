package com.artillexstudios.axapi;

import com.artillexstudios.axapi.dependencies.DependencyManagerWrapper;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.gui.AnvilListener;
import com.artillexstudios.axapi.gui.inventory.InventoryUpdater;
import com.artillexstudios.axapi.gui.inventory.listener.InventoryClickListener;
import com.artillexstudios.axapi.gui.inventory.renderer.InventoryRenderers;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.listeners.BuiltinPacketListener;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.particle.ParticleTypes;
import com.artillexstudios.axapi.placeholders.PlaceholderAPIHook;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.PaperUtils;
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
import revxrsal.zapper.DependencyManager;
import revxrsal.zapper.classloader.URLClassLoaderWrapper;

import java.io.File;
import java.net.URLClassLoader;

public abstract class AxPlugin extends JavaPlugin {
    public EntityTracker tracker;

    public AxPlugin() {
        DependencyManager manager = new DependencyManager(this.getDescription(), new File(this.getDataFolder(), "libs"), URLClassLoaderWrapper.wrap((URLClassLoader) this.getClassLoader()));
        DependencyManagerWrapper wrapper = new DependencyManagerWrapper(manager);
        wrapper.dependency("org{}apache{}commons:commons-math3:3.6.1");
        wrapper.dependency("com{}github{}ben-manes{}caffeine:caffeine:3.1.8");

        wrapper.relocate("org{}apache{}commons{}math3", "com.artillexstudios.axapi.libs.math3");
        wrapper.relocate("com{}github{}benmanes", "com.artillexstudios.axapi.libs.caffeine");
        if (!PaperUtils.isPaper()) {
            wrapper.dependency("net{}kyori:adventure-api:4.24.0");
        }

        this.dependencies(wrapper);
        manager.load();

        FeatureFlags.refresh(this);
        this.updateFlags();
    }

    public void updateFlags() {

    }

    @Override
    public void onEnable() {
        if (!NMSHandlers.British.initialise(this)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        ComponentSerializer.INSTANCE.refresh();
        DataComponents.setDataComponentImpl(NMSHandlers.getNmsHandler().dataComponents());
        Scheduler.scheduler.init(this);

        if (FeatureFlags.PACKET_ENTITY_TRACKER_ENABLED.get()) {
            this.tracker = new EntityTracker(this);
            this.tracker.startTicking();
        }

        if (FeatureFlags.ENABLE_PACKET_LISTENERS.get()) {
            PacketEvents.INSTANCE.addListener(new BuiltinPacketListener(this.tracker));
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
                InventoryRenderers.disconnect(event.getPlayer().getUniqueId());
                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(event.getPlayer());
                if (FeatureFlags.ENABLE_PACKET_LISTENERS.get()) {
                    wrapper.uninject();
                }

                if (AxPlugin.this.tracker == null) {
                    return;
                }

                AxPlugin.this.tracker.untrackFor(ServerPlayerWrapper.wrap(event.getPlayer()));
            }

            @EventHandler
            public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(event.getPlayer());
                if (!FeatureFlags.ENABLE_PACKET_LISTENERS.get()) {
                    return;
                }

                wrapper.inject();
            }

            @EventHandler
            public void onPacketEntityInteractEvent(@NotNull final PacketEntityInteractEvent event) {
                event.getPacketEntity().callInteract(event);
            }

            @EventHandler
            public void onPlayerChangedWorldEvent(@NotNull final PlayerChangedWorldEvent event) {
                if (AxPlugin.this.tracker == null) {
                    return;
                }

                AxPlugin.this.tracker.untrackFor(ServerPlayerWrapper.wrap(event.getPlayer()));
            }
        }, this);
        Bukkit.getPluginManager().registerEvents(new AnvilListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);

        if (FeatureFlags.HOLOGRAM_UPDATE_TICKS.get() > 0) {
            Holograms.startTicking();
        }

        if (FeatureFlags.USE_INVENTORY_UPDATER.get()) {
            InventoryUpdater.INSTANCE.start(this);
        }

        ParticleTypes.init();
        ClientboundPacketTypes.init();
        ServerboundPacketTypes.init();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
            wrapper.inject();
        }

        this.enable();

        if (FeatureFlags.PLACEHOLDER_API_HOOK.get() && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }
    }

    public void enable() {

    }

    @Override
    public void onLoad() {
        this.load();
    }

    public void dependencies(DependencyManagerWrapper manager) {

    }

    public void load() {

    }

    @Override
    public void onDisable() {
        this.disable();
        Scheduler.get().cancelAll();

        for (Player player : Bukkit.getOnlinePlayers()) {
            ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
            wrapper.uninject();
        }

        if (this.tracker != null) {
            this.tracker.shutdown();
        }
        Holograms.shutdown();
        InventoryUpdater.INSTANCE.shutdown();
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
}