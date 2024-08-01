package com.artillexstudios.axapi.packetentity.tracker;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.artillexstudios.axapi.utils.Version;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class EntityTracker {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final Int2ObjectMap<TrackedEntity> entityMap = new Int2ObjectLinkedOpenHashMap<>();
    private final FastFieldAccessor accessor = FastFieldAccessor.forClassField(String.format("com.artillexstudios.axapi.nms.%s.entity.PacketEntity", Version.getServerVersion().nmsVersion), "tracker");

    public PacketEntity getById(int id) {
        this.readLock.lock();
        try {
            TrackedEntity entity = this.entityMap.get(id);
            return entity == null ? null : entity.entity;
        } finally {
            this.readLock.unlock();
        }
    }

    public void addEntity(PacketEntity entity) {
        TrackedEntity trackedEntity = new TrackedEntity(entity);
        this.accessor.set(entity, trackedEntity);

        this.writeLock.lock();
        try {
            this.entityMap.put(entity.id(), trackedEntity);

            trackedEntity.updateTracking(trackedEntity.getPlayersInTrackingRange());
        } finally {
            this.writeLock.unlock();
        }
    }

    public void removeEntity(PacketEntity entity) {
        this.writeLock.lock();
        try {
            TrackedEntity trackedEntity = this.entityMap.remove(entity.id());

            if (trackedEntity != null) {
                trackedEntity.broadcastRemove();
            }

            this.accessor.set(entity, null);
        } finally {
            this.writeLock.unlock();
        }
    }

    public void untrackFor(Player player) {
        this.readLock.lock();
        try {
            for (Int2ObjectMap.Entry<TrackedEntity> value : this.entityMap.int2ObjectEntrySet()) {
                TrackedEntity tracker = value.getValue();
                tracker.untrack(player);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void process() {
        this.readLock.lock();
        try {
            for (TrackedEntity entity : this.entityMap.values()) {
                entity.updateTracking(entity.getPlayersInTrackingRange());
                entity.entity.sendChanges();
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public static class TrackedEntity {
        public final Set<Player> seenBy = ReferenceSets.synchronize(new ReferenceOpenHashSet<>());
        private final PacketEntity entity;
        private final World world;
        private Player[] lastTrackerCandidates;

        public TrackedEntity(PacketEntity entity) {
            this.entity = entity;
            this.world = this.entity.location().getWorld();
        }

        public void updateTracking(Player[] newTrackerCandidates) {
            Player[] oldTrackerCandidates = this.lastTrackerCandidates;
            this.lastTrackerCandidates = newTrackerCandidates;

            Location location = null;
            if (newTrackerCandidates != null) {
                location = new Location(this.entity.location().getWorld(), 0, 0, 0);

                for (Player raw : newTrackerCandidates) {
                    this.updatePlayer(raw, raw.getLocation(location));
                }
            }

            if (oldTrackerCandidates != null && Arrays.equals(oldTrackerCandidates, newTrackerCandidates)) {
                return;
            }

            if (location == null) {
                location = new Location(this.entity.location().getWorld(), 0, 0, 0);
            }

            for (Player player : this.seenBy) {
                if (newTrackerCandidates == null || ObjectArrays.binarySearch(newTrackerCandidates, player) < 0) {
                    this.updatePlayer(player, player.getLocation(location));
                }
            }
        }

        public void updatePlayer(Player player, Location location) {
            double dx = location.getX() - this.entity.location().getX();
            double dz = location.getZ() - this.entity.location().getZ();
            double d1 = dx * dx + dz * dz;
            boolean flag = d1 <= 32 * 32;


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

        public Player[] getPlayersInTrackingRange() {
            return NMSHandlers.getNmsHandler().players(this.world);
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
