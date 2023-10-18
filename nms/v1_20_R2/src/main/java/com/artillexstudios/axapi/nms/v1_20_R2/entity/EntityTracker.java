package com.artillexstudios.axapi.nms.v1_20_R2.entity;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;

import java.util.List;
import java.util.Set;

public class EntityTracker implements PacketEntityTracker {
    private final Int2ObjectMap<TrackedEntity> entityMap = new Int2ObjectOpenHashMap<>();

    @Override
    public com.artillexstudios.axapi.entity.impl.PacketEntity getById(int id) {
        return entityMap.get(id).entity;
    }

    @Override
    public void addEntity(com.artillexstudios.axapi.entity.impl.PacketEntity entity) {
        var packetEntity = (PacketEntity) entity;
        var trackedEntity = new TrackedEntity(packetEntity);
        packetEntity.tracker = trackedEntity;

        entityMap.put(packetEntity.entityId, trackedEntity);

        trackedEntity.updateTracking(trackedEntity.getPlayersInTrackingRange());
    }

    @Override
    public void removeEntity(com.artillexstudios.axapi.entity.impl.PacketEntity entity) {
        var packetEntity = (PacketEntity) entity;
        var trackedEntity = entityMap.remove(packetEntity.entityId);

        if (trackedEntity != null) {
            trackedEntity.broadcastRemove();
        }

        packetEntity.tracker = null;
    }

    public void process() {
        for (TrackedEntity tracker : entityMap.values()) {
            tracker.updateTracking(tracker.getPlayersInTrackingRange());
        }

        for (TrackedEntity tracker : entityMap.values()) {
            tracker.entity.sendChanges();
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
                for (ServerPlayer raw : newTrackerCandidates) {
                    this.updatePlayer(raw);
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

            if (flag) {
                if (this.seenBy.add(player.connection)) {
                    entity.addPairing(player);
                }
            } else if (this.seenBy.remove(player.connection)) {
                entity.removePairing(player);
            }
        }

        public List<ServerPlayer> getPlayersInTrackingRange() {
            var location = entity.getLocation();

            return ((CraftWorld) location.getWorld()).getHandle().players();
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