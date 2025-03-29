package com.artillexstudios.axapi;

import com.artillexstudios.axapi.dependencies.DependencyManagerWrapper;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.gui.AnvilListener;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundBlockUpdateWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundInteractWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundSignUpdateWrapper;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.placeholders.PlaceholderAPIHook;
import com.artillexstudios.axapi.placeholders.Placeholders;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.artillexstudios.shared.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.shared.axapi.packet.PacketEvent;
import com.artillexstudios.shared.axapi.packet.PacketEvents;
import com.artillexstudios.shared.axapi.packet.PacketListener;
import com.artillexstudios.shared.axapi.packet.ServerboundPacketTypes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.zapper.DependencyManager;
import revxrsal.zapper.classloader.URLClassLoaderWrapper;

import java.io.File;
import java.net.URLClassLoader;

public abstract class AxPlugin extends JavaPlugin {
    public EntityTracker tracker;
    private final FeatureFlags flags = new FeatureFlags(this);
    private ComponentSerializer serializer;
    private PacketEvents packetEvents;

    public AxPlugin() {
        DependencyManager manager = new DependencyManager(this.getDescription(), new File(this.getDataFolder(), "libs"), URLClassLoaderWrapper.wrap((URLClassLoader) this.getClassLoader()));
        DependencyManagerWrapper wrapper = new DependencyManagerWrapper(manager);
        wrapper.dependency("org{}apache{}commons:commons-math3:3.6.1");
        wrapper.dependency("com{}github{}ben-manes{}caffeine:caffeine:3.1.8");

        wrapper.relocate("org{}apache{}commons{}math3", "com.artillexstudios.axapi.libs.math3");
        wrapper.relocate("com{}github{}benmanes", "com.artillexstudios.axapi.libs.caffeine");

        this.dependencies(wrapper);
        manager.load();

        this.updateFlags(this.flags);
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
        serializer = new ComponentSerializer(this);

        if (this.flags.PACKET_ENTITY_TRACKER_ENABLED.get()) {
            tracker = new EntityTracker(this.flags);
            tracker.startTicking();
        }

        RegisteredServiceProvider<PacketEvents> registration = Bukkit.getServicesManager().getRegistration(PacketEvents.class);
        if (registration == null) {
            this.packetEvents = new PacketEvents();
            Bukkit.getServicesManager().register(PacketEvents.class, this.packetEvents, this, ServicePriority.High);
        } else {
            this.packetEvents = registration.getProvider();
        }
        this.packetEvents.addListener(new PacketListener() {
            @Override
            public void onPacketReceive(PacketEvent event) {
                if (event.type() == ServerboundPacketTypes.INTERACT) {
                    if (AxPlugin.this.tracker == null) {
                        LogUtils.info("No tracker. Package: {}, class: {}", AxPlugin.class.getPackageName(), AxPlugin.class);
                        return;
                    }

                    ServerboundInteractWrapper wrapper = new ServerboundInteractWrapper(event);
                    PacketEntity entity = tracker.getById(wrapper.entityId());
                    if (entity != null) {
                        PacketEntityInteractEvent interactEvent = new PacketEntityInteractEvent(event.player(), entity, wrapper.type() == ServerboundInteractWrapper.ActionType.ATTACK, wrapper.action() instanceof ServerboundInteractWrapper.InteractionAtLocationAction action ? action.location() : null, wrapper.action() instanceof ServerboundInteractWrapper.InteractionAction action ? action.hand() : wrapper.action() instanceof ServerboundInteractWrapper.InteractionAtLocationAction interaction ? interaction.hand() : null);
                        Bukkit.getPluginManager().callEvent(interactEvent);
                    }
                } else if (event.type() == ServerboundPacketTypes.SIGN_UPDATE) {
                    ServerboundSignUpdateWrapper wrapper = new ServerboundSignUpdateWrapper(event);
                    SignInput signInput = SignInput.remove(event.player());

                    if (signInput == null) {
                        return;
                    }

                    signInput.getListener().accept(event.player(), wrapper.lines());
                    com.artillexstudios.axapi.scheduler.Scheduler.get().runAt(signInput.getLocation(), task -> {
                        ServerPlayerWrapper playerWrapper = ServerPlayerWrapper.wrap(event.player());
                        playerWrapper.sendPacket(new ClientboundBlockUpdateWrapper(signInput.getLocation(), signInput.getLocation().getBlock().getType()));
                    });
                }
            }
        });

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(event.getPlayer());
                wrapper.uninject();

                if (tracker == null) {
                    return;
                }

                tracker.untrackFor(ServerPlayerWrapper.wrap(event.getPlayer()));
            }

            @EventHandler
            public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(event.getPlayer());
                wrapper.inject();
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

        if (this.flags.HOLOGRAM_UPDATE_TICKS.get() > 0) {
            Holograms.startTicking();
        }

        ClientboundPacketTypes.init();
        ServerboundPacketTypes.init();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
            wrapper.inject();
        }

        this.enable();

        Placeholders.lock();
        if (this.flags.PLACEHOLDER_API_HOOK.get() && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook().register();
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
            wrapper.inject();
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

    public FeatureFlags flags() {
        return this.flags;
    }

    public ComponentSerializer serializer() {
        return this.serializer;
    }

    public long reloadWithTime() {
        long start = System.currentTimeMillis();
        this.reload();

        return System.currentTimeMillis() - start;
    }
}