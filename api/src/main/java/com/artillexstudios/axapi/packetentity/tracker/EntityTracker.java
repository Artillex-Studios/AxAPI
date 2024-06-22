package com.artillexstudios.axapi.packetentity.tracker;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.artillexstudios.axapi.utils.Version;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class EntityTracker {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final Int2ObjectMap<TrackedEntity> entityMap = new Int2ObjectOpenHashMap<>();
    private final FastFieldAccessor accessor = FastFieldAccessor.forClassField(String.format("com.artillexstudios.axapi.nms.%s.entity.PacketEntity", Version.getServerVersion().nmsVersion), "tracker");

    public PacketEntity getById(int id) {
        readLock.lock();
        try {
            TrackedEntity entity = entityMap.get(id);
            return entity == null ? null : entity.entity;
        } finally {
            readLock.unlock();
        }
    }

    public void addEntity(PacketEntity entity) {
        TrackedEntity trackedEntity = new TrackedEntity(entity);
        accessor.set(entity, trackedEntity);

        writeLock.lock();
        try {
            entityMap.put(entity.id(), trackedEntity);

            trackedEntity.updateTracking(trackedEntity.getPlayersInTrackingRange());
        } finally {
            writeLock.unlock();
        }
    }

    public void removeEntity(PacketEntity entity) {
        writeLock.lock();
        try {
            TrackedEntity trackedEntity = entityMap.remove(entity.id());

            if (trackedEntity != null) {
                trackedEntity.broadcastRemove();
            }

            accessor.set(entity, null);
        } finally {
            writeLock.unlock();
        }
    }

    public void untrackFor(Player player) {
        readLock.lock();
        try {
            for (Int2ObjectMap.Entry<TrackedEntity> value : entityMap.int2ObjectEntrySet()) {
                TrackedEntity tracker = value.getValue();
                tracker.untrack(player);
            }
        } finally {
            readLock.unlock();
        }
    }

    public void process() {
        readLock.lock();
        try {
            for (TrackedEntity entity : entityMap.values()) {
                entity.updateTracking(entity.getPlayersInTrackingRange());
                entity.entity.sendChanges();
            }
        } finally {
            readLock.unlock();
        }
    }

    public static class TrackedEntity {
        public final Set<Player> seenBy = ReferenceSets.synchronize(new ReferenceOpenHashSet<>());
        private final PacketEntity entity;
        private List<Player> lastTrackerCandidates;

        public TrackedEntity(PacketEntity entity) {
            this.entity = entity;
        }

        public void updateTracking(List<Player> newTrackerCandidates) {
            List<Player> oldTrackerCandidates = this.lastTrackerCandidates;
            this.lastTrackerCandidates = newTrackerCandidates;

            Location location = new Location(entity.location().getWorld(), 0, 0, 0);
            if (newTrackerCandidates != null) {
                for (Player raw : newTrackerCandidates) {
                    this.updatePlayer(raw, raw.getLocation(location));
                }
            }

            if (oldTrackerCandidates == newTrackerCandidates) {
                return;
            }

            for (Player player : seenBy) {
                if (newTrackerCandidates == null || !newTrackerCandidates.contains(player)) {
                    this.updatePlayer(player, player.getLocation(location));
                }
            }
        }

        public void updatePlayer(Player player, Location location) {
            double dx = location.getX() - this.entity.location().getX();
            double dz = location.getZ() - this.entity.location().getZ();
            double d1 = dx * dx + dz * dz;
            boolean flag = d1 <= 32 * 32;


            if (flag && !entity.canSee(player)) {
                flag = false;
            }

            if (flag) {
                if (this.seenBy.add(player)) {
                    entity.addPairing(player);
                }
            } else if (this.seenBy.remove(player)) {
                entity.removePairing(player);
            }
        }

        public void untrack(Player player) {
            if (!this.seenBy.remove(player)) return;

            entity.removePairing(player);
        }

        public List<Player> getPlayersInTrackingRange() {
            return entity.location().getWorld().getPlayers();
        }

        public void broadcast(Object packet) {
            for (Player player : this.seenBy) {
                NMSHandlers.getNmsHandler().sendPacket(player, packet);
            }
        }

        public void broadcastRemove() {
            for (Player player : this.seenBy) {
                entity.removePairing(player);
            }
        }
    }
}
