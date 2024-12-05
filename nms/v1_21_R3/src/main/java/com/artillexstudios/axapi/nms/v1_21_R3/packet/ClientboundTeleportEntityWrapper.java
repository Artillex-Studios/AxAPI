package com.artillexstudios.axapi.nms.v1_21_R3.packet;

import com.artillexstudios.axapi.packetentity.PacketEntity;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

import java.util.Set;

public class ClientboundTeleportEntityWrapper {

    public static ClientboundTeleportEntityPacket createNew(PacketEntity entity, Location location) {
        return ClientboundTeleportEntityPacket.teleport(entity.id(), new PositionMoveRotation(new Vec3(location.getX(), location.getY(), location.getZ()), Vec3.ZERO, location.getYaw(), location.getPitch() * 256.0F / 360.0F), Set.of(), true);
    }
}
