package com.artillexstudios.axapi;

import com.artillexstudios.axapi.dependencies.DependencyManagerWrapper;
import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.gui.AnvilListener;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.gui.inventory.InventoryRenderers;
import com.artillexstudios.axapi.gui.inventory.listener.InventoryClickListener;
import com.artillexstudios.axapi.hologram.Holograms;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketListener;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundAddEntityWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundBlockUpdateWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetPassengersWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundInteractWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundSignUpdateWrapper;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.particle.ParticleTypes;
import com.artillexstudios.axapi.placeholders.PlaceholderAPIHook;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.ComponentSerializer;
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

        PacketEvents.INSTANCE.addListener(new PacketListener() {

            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.type() == ClientboundPacketTypes.ADD_ENTITY) {
                    if (AxPlugin.this.tracker == null) {
                        return;
                    }

                    ClientboundAddEntityWrapper wrapper = new ClientboundAddEntityWrapper(event);
                    int entityId = wrapper.entityId();
                    PacketEntity rider = AxPlugin.this.tracker.findRider(entityId);

                    if (rider == null) {
                        return;
                    }

                    ClientboundSetPassengersWrapper passengersWrapper = new ClientboundSetPassengersWrapper(entityId, new int[]{rider.id()});
                    ServerPlayerWrapper serverPlayerWrapper = ServerPlayerWrapper.wrap(event.player());
                    serverPlayerWrapper.sendPacket(passengersWrapper);
                }
            }

            @Override
            public void onPacketReceive(PacketEvent event) {
                if (event.type() == ServerboundPacketTypes.INTERACT) {
                    if (AxPlugin.this.tracker == null) {
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
                InventoryRenderers.disconnect(event.getPlayer().getUniqueId());
                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(event.getPlayer());
                wrapper.uninject();

                if (AxPlugin.this.tracker == null) {
                    return;
                }

                AxPlugin.this.tracker.untrackFor(ServerPlayerWrapper.wrap(event.getPlayer()));
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