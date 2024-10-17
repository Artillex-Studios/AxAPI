package com.artillexstudios.axapi.packetentity.tracker;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.artillexstudios.axapi.utils.ExceptionReportingScheduledThreadPool;
import com.artillexstudios.axapi.utils.FeatureFlags;
import com.artillexstudios.axapi.utils.LogUtils;
import com.artillexstudios.axapi.utils.PaperUtils;
import com.artillexstudios.axapi.utils.Version;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public final class EntityTracker {
    private static final boolean folia = PaperUtils.isFolia();
    private final StampedLock lock = new StampedLock();
    private final Int2ObjectMap<TrackedEntity> entityMap = new Int2ObjectLinkedOpenHashMap<>();
    private final FastFieldAccessor accessor = FastFieldAccessor.forClassField(String.format("com.artillexstudios.axapi.nms.%s.entity.PacketEntity", Version.getServerVersion().nmsVersion), "tracker");
    private final JavaPlugin instance = AxPlugin.getPlugin(AxPlugin.class);
    private ScheduledExecutorService service;

    public void startTicking() {
        this.shutdown();
        this.service = new ExceptionReportingScheduledThreadPool(FeatureFlags.PACKET_ENTITY_TRACKER_THREADS.get(), new ThreadFactoryBuilder().setNameFormat(this.instance.getName() + "-EntityTracker-%s").build());
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
                    if (FeatureFlags.DEBUG.get()) {
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
        long stamp = this.lock.readLock();
        try {
            TrackedEntity entity = this.entityMap.get(id);
            return entity == null ? null : entity.entity;
        } finally {
            this.lock.unlockRead(stamp);
        }
    }

    public void addEntity(PacketEntity entity) {
        TrackedEntity trackedEntity = new TrackedEntity(entity);
        this.accessor.set(entity, trackedEntity);

        long stamp = this.lock.writeLock();
        try {
            this.entityMap.put(entity.id(), trackedEntity);

            trackedEntity.updateTracking(trackedEntity.getPlayersInTrackingRange());
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    public void removeEntity(PacketEntity entity) {
        long stamp = this.lock.writeLock();
        try {
            TrackedEntity trackedEntity = this.entityMap.remove(entity.id());

            if (trackedEntity != null) {
                trackedEntity.broadcastRemove();
            }

            this.accessor.set(entity, null);
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    public void untrackFor(Player player) {
        long stamp = this.lock.readLock();
        try {
            for (Int2ObjectMap.Entry<TrackedEntity> value : this.entityMap.int2ObjectEntrySet()) {
                TrackedEntity tracker = value.getValue();
                tracker.untrack(player);
            }
        } finally {
            this.lock.unlockRead(stamp);
        }
    }

    public void process() {
        long stamp = this.lock.readLock();
        try {
            for (TrackedEntity entity : this.entityMap.values()) {
                entity.updateTracking(entity.getPlayersInTrackingRange());
                entity.entity.sendChanges();
            }
        } finally {
            this.lock.unlockRead(stamp);
        }
    }

    public static class TrackedEntity {
        public final Set<Player> seenBy = ReferenceSets.synchronize(new ReferenceOpenHashSet<>());
        private final PacketEntity entity;
        private final World world;
        private List<Player> lastTrackerCandidates;

        public TrackedEntity(PacketEntity entity) {
            this.entity = entity;
            this.world = this.entity.location().getWorld();
        }

        public void updateTracking(List<Player> newTrackerCandidates) {
            List<Player> oldTrackerCandidates = this.lastTrackerCandidates;
            this.lastTrackerCandidates = newTrackerCandidates;

            Location location = null;
            if (newTrackerCandidates != null) {
                location = new Location(this.entity.location().getWorld(), 0, 0, 0);

                for (Player raw : newTrackerCandidates) {
                    this.updatePlayer(raw, raw.getLocation(location));
                }
            }

            if (oldTrackerCandidates != null && Objects.equals(this.lastTrackerCandidates, newTrackerCandidates)) {
                return;
            }

            if (location == null) {
                location = new Location(this.entity.location().getWorld(), 0, 0, 0);
            }

            for (Player player : this.seenBy) {
                if (newTrackerCandidates == null || !newTrackerCandidates.contains(player)) {
                    this.updatePlayer(player, player.getLocation(location));
                }
            }
        }

        public void updatePlayer(Player player, Location location) {
            double dx = location.getX() - this.entity.location().getX();
            double dz = location.getZ() - this.entity.location().getZ();
            double d1 = dx * dx + dz * dz;
            boolean flag = d1 <= this.entity.viewDistanceSquared();

            if (flag && !this.entity.canSee(player)) {
                flag = false;
            }

            if (flag) {
                if (this.seenBy.add(player)) {
                    this.entity.addPairing(player);
                }
            } else if (this.seenBy.remove(player)) {
                this.entity.removePairing(player);
            }
        }

        public void untrack(Player player) {
            if (!this.seenBy.remove(player)) {
                return;
            }

            this.entity.removePairing(player);
        }

        public List<Player> getPlayersInTrackingRange() {
            return folia ? this.world.getPlayers() : NMSHandlers.getNmsHandler().players(this.world);
        }

        public void broadcast(Object packet) {
            for (Player player : this.seenBy) {
                NMSHandlers.getNmsHandler().sendPacket(player, packet);
            }
        }

        public void broadcastRemove() {
            for (Player player : this.seenBy) {
                this.entity.removePairing(player);
            }
        }
    }
}
