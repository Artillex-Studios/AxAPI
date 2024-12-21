package com.artillexstudios.axapi.packetentity.tracker;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.collections.IdentityArrayMap;
import com.artillexstudios.axapi.collections.RawReferenceOpenHashSet;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.artillexstudios.axapi.utils.ExceptionReportingScheduledThreadPool;
import com.artillexstudios.axapi.utils.LogUtils;
import com.artillexstudios.axapi.utils.PaperUtils;
import com.artillexstudios.axapi.utils.Version;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class EntityTracker {
    private static final boolean folia = PaperUtils.isFolia();
    private final ConcurrentHashMap<Integer, TrackedEntity> entityMap = new ConcurrentHashMap<>();
    private final FastFieldAccessor accessor = FastFieldAccessor.forClassField(String.format("com.artillexstudios.axapi.nms.%s.entity.PacketEntity", Version.getServerVersion().nmsVersion), "tracker");
    private final JavaPlugin instance = AxPlugin.getPlugin(AxPlugin.class);
    private ScheduledExecutorService service;

    public void startTicking() {
        this.shutdown();
        this.service = new ExceptionReportingScheduledThreadPool(AxPlugin.flags().PACKET_ENTITY_TRACKER_THREADS.get(), new ThreadFactoryBuilder().setNameFormat(this.instance.getName() + "-EntityTracker-%s").build());
        this.service.scheduleAtFixedRate(() -> {
            try {
                this.process();
            } catch (Exception exception) {
                if (exception instanceof ConcurrentModificationException) {
                    // There's something weird with the entity tracker after the server starts up.
                    // Nothing blew up yet, so I guess the error is safe to ignore...
                    // If something blows up, I'm not the person to blame!
                    // (But people don't like seeing errors, so this is the solution until I find out what causes the tracker to throw a CME)
                    //
                    // Please don't hunt me for this, I didn't want to do it.
                    if (AxPlugin.flags().DEBUG.get()) {
                        LogUtils.error("Caught ConcurrentModificationException when processing packet entities!", exception);
                    }
                    return;
                }

                LogUtils.error("An unexpected error occurred while processing packet entities via the tracker!", exception);
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        if (this.service == null) {
            return;
        }

        this.service.shutdown();
        try {
            this.service.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            LogUtils.error("Failed to shut down EntityTracker service!", exception);
        }
    }

    public PacketEntity getById(int id) {
        TrackedEntity entity = this.entityMap.get(id);
        return entity == null ? null : entity.entity;
    }

    public void addEntity(PacketEntity entity) {
        TrackedEntity trackedEntity = new TrackedEntity(entity);
        this.accessor.setVolatile(entity, trackedEntity);

        this.entityMap.put(entity.id(), trackedEntity);

        trackedEntity.updateTracking(TrackedEntity.getPlayersInWorld(entity.location().getWorld()));
    }

    public void removeEntity(PacketEntity entity) {
        TrackedEntity trackedEntity = this.entityMap.remove(entity.id());

        if (trackedEntity != null) {
            trackedEntity.broadcastRemove();
        }

        this.accessor.set(entity, null);
    }

    public void untrackFor(ServerPlayerWrapper player) {
        for (EntityTracker.TrackedEntity tracker : this.entityMap.values()) {
            tracker.untrack(player);
        }
    }

    public void process() {
        // We can safely keep a cache of players in the worlds, as we can spare tracking a new player a tick later
        // This also reduces the strain on the GC as less objects are wasted (ServerPlayerWrapper)
        // We are only ever reading from this map, so thread safety doesn't matter
        List<World> worlds = Bukkit.getWorlds();
        Map<World, List<ServerPlayerWrapper>> tracking = new IdentityArrayMap<>(worlds.size() + 1);
        for (World world : worlds) {
            tracking.put(world, TrackedEntity.getPlayersInWorld(world));
        }

        AtomicInteger integer = new AtomicInteger();
        TrackedEntity[] array = this.entityMap.values().toArray(new TrackedEntity[0]);
        int length = array.length;
        for (int i = 0; i < AxPlugin.flags().PACKET_ENTITY_TRACKER_THREADS.get(); i++) {
            this.service.execute(() -> {
                int l = length;
                int index;
                while ((index = integer.getAndIncrement()) < l) {
                    TrackedEntity tracked = array[index];
                    tracked.preTick();
                    List<ServerPlayerWrapper> players = tracking.get(tracked.world);
                    if (players == null) {
                        LogUtils.error("Failed to track entity in world {}, due to tracker players being null! Tracking: {}", tracked.world.getName(), tracking);
                        continue;
                    }

                    tracked.updateTracking(players);
                    if (tracked.hasViewers()) {
                        tracked.entity.sendChanges();
                    }
                }
            });
        }
    }

    public static class TrackedEntity {
        public final ReferenceSet<Object> seenBy = ReferenceSets.synchronize(new RawReferenceOpenHashSet<>());
        private final PacketEntity entity;
        private final World world;
        private List<ServerPlayerWrapper> lastTrackerCandidates;
        private boolean hasViewers = false;

        public TrackedEntity(PacketEntity entity) {
            this.entity = entity;
            this.world = this.entity.location().getWorld();
        }

        public static List<ServerPlayerWrapper> getPlayersInWorld(World world) {
            if (world == null) {
                return ImmutableList.of();
            }

            if (folia) {
                List<Player> players = world.getPlayers();
                List<ServerPlayerWrapper> wrapper = new ArrayList<>(players.size());
                for (Player player : players) {
                    wrapper.add(ServerPlayerWrapper.wrap(player));
                }

                return wrapper;
            }

            return NMSHandlers.getNmsHandler().players(world);
        }

        public void updateTracking(@NotNull List<ServerPlayerWrapper> newTrackerCandidates) {
            List<ServerPlayerWrapper> oldTrackerCandidates = this.lastTrackerCandidates;
            this.lastTrackerCandidates = newTrackerCandidates;

            for (ServerPlayerWrapper raw : newTrackerCandidates) {
                this.updatePlayer(raw, false);
            }

            if (oldTrackerCandidates != null && oldTrackerCandidates.size() == newTrackerCandidates.size()) {
                return;
            }

            for (Object player : RawReferenceOpenHashSet.rawSet(this.seenBy)) {
                if (player == null) {
                    continue;
                }

                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
                if (newTrackerCandidates.isEmpty() || !newTrackerCandidates.contains(wrapper)) {
                    this.updatePlayer(wrapper, true);
                }
            }
        }

        public void updatePlayer(ServerPlayerWrapper player, boolean removeStage) {
            double dx = player.getX() - this.entity.location().getX();
            double dz = player.getZ() - this.entity.location().getZ();
            double d1 = dx * dx + dz * dz;
            boolean flag = d1 <= this.entity.viewDistanceSquared();

            if (flag && !this.entity.canSee(player.wrapped())) {
                flag = false;
                removeStage = true;
            }

            if (!removeStage && flag) {
                this.hasViewers = true;
                if (this.seenBy.add(player.asMinecraft())) {
                    this.entity.addPairing(player.wrapped());
                }
            } else if (removeStage && this.seenBy.remove(player.asMinecraft())) {
                this.entity.removePairing(player.wrapped());
            }
        }

        public void untrack(ServerPlayerWrapper player) {
            if (!this.seenBy.remove(player.asMinecraft())) {
                return;
            }

            this.entity.removePairing(player.wrapped());
        }

        public void broadcast(Object packet) {
            this.seenBy.forEach(player -> {
                NMSHandlers.getNmsHandler().sendPacket(ServerPlayerWrapper.wrap(player), packet);
            });
        }

        public void broadcastRemove() {
            this.seenBy.forEach(player -> {
                this.entity.removePairing(ServerPlayerWrapper.wrap(player).wrapped());
            });
        }

        public void preTick() {
            this.hasViewers = false;
        }

        public boolean hasViewers() {
            return this.hasViewers;
        }
    }
}
