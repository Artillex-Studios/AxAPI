package com.artillexstudios.axapi.nms.v1_19_R3.entity;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EntityTracker implements PacketEntityTracker {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Int2ObjectMap<TrackedEntity> entityMap = new Int2ObjectOpenHashMap<>();

    @Override
    public com.artillexstudios.axapi.entity.impl.PacketEntity getById(int id) {
        lock.readLock().lock();
        try {
            TrackedEntity entity = entityMap.get(id);
            return entity == null ? null : entity.entity;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void addEntity(com.artillexstudios.axapi.entity.impl.PacketEntity entity) {
        PacketEntity packetEntity = (PacketEntity) entity;
        TrackedEntity trackedEntity = new TrackedEntity(packetEntity);
        packetEntity.tracker = trackedEntity;

        lock.writeLock().lock();
        try {
            entityMap.put(packetEntity.entityId, trackedEntity);

            trackedEntity.updateTracking(trackedEntity.getPlayersInTrackingRange());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeEntity(com.artillexstudios.axapi.entity.impl.PacketEntity entity) {
        var packetEntity = (PacketEntity) entity;

        lock.writeLock().lock();
        try {
            TrackedEntity trackedEntity = entityMap.remove(packetEntity.entityId);

            if (trackedEntity != null) {
                trackedEntity.broadcastRemove();
            }

            packetEntity.tracker = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void untrackFor(Player player) {
        lock.readLock().lock();
        try {
            for (Int2ObjectMap.Entry<TrackedEntity> value : entityMap.int2ObjectEntrySet()) {
                TrackedEntity tracker = value.getValue();
                tracker.untrack(player);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void process() {
        lock.readLock().lock();
        try {
            for (Int2ObjectMap.Entry<TrackedEntity> value : entityMap.int2ObjectEntrySet()) {
                TrackedEntity tracker = value.getValue();
                tracker.updateTracking(tracker.getPlayersInTrackingRange());
            }

            for (Int2ObjectMap.Entry<TrackedEntity> value : entityMap.int2ObjectEntrySet()) {
                value.getValue().entity.sendChanges();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public static class TrackedEntity {
        public final Set<ServerPlayerConnection> seenBy = ReferenceSets.synchronize(new ReferenceOpenHashSet<>());
        private final PacketEntity entity;
        private List<ServerPlayer> lastTrackerCandidates;

        public TrackedEntity(PacketEntity entity) {
            this.entity = entity;
        }

        public void updateTracking(List<ServerPlayer> newTrackerCandidates) {
            List<ServerPlayer> oldTrackerCandidates = this.lastTrackerCandidates;
            this.lastTrackerCandidates = newTrackerCandidates;

            if (newTrackerCandidates != null) {
                for (ServerPlayer player : newTrackerCandidates) {
                    this.updatePlayer(player);
                }
            }

            if (oldTrackerCandidates == newTrackerCandidates) {
                return;
            }

            for (ServerPlayerConnection conn : this.seenBy.toArray(new ServerPlayerConnection[0])) {
                if (newTrackerCandidates == null || !newTrackerCandidates.contains(conn.getPlayer())) {
                    this.updatePlayer(conn.getPlayer());
                }
            }
        }

        public void updatePlayer(ServerPlayer player) {
            double dx = player.getX() - this.entity.getLocation().getX();
            double dz = player.getZ() - this.entity.getLocation().getZ();
            double d1 = dx * dx + dz * dz;
            boolean flag = d1 <= entity.getViewDistanceSquared();
            if (!player.getLevel().uuid.equals(this.entity.level.uuid)) {
                flag = false;
            }

            if (!entity.canSee(player.getBukkitEntity())) {
                flag = false;
            }

            if (flag) {
                if (this.seenBy.add(player.connection)) {
                    entity.addPairing(player);
                }
            } else if (this.seenBy.remove(player.connection)) {
                entity.removePairing(player);
            }
        }
        
        public void untrack(Player player) {
            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            if (!this.seenBy.remove(serverPlayer.connection))
                return;

            entity.removePairing(serverPlayer);
        }

        public List<ServerPlayer> getPlayersInTrackingRange() {
            return entity.level.players();
        }

        public void broadcast(Packet<?> packet) {
            for (ServerPlayerConnection serverPlayerConnection : this.seenBy) {
                serverPlayerConnection.send(packet);
            }
        }

        public void broadcastRemove() {
            for (ServerPlayerConnection serverPlayerConnection : this.seenBy) {
                entity.removePairing(serverPlayerConnection.getPlayer());
            }
        }
    }
}
