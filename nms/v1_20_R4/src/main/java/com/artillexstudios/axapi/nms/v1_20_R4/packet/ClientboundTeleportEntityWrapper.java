package com.artillexstudios.axapi.nms.v1_20_R4.packet;

import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Location;

public class ClientboundTeleportEntityWrapper {
    private static final FastFieldAccessor entityId = FastFieldAccessor.forClassField(ClientboundTeleportEntityPacket.class, "b");
    private static final FastFieldAccessor x = FastFieldAccessor.forClassField(ClientboundTeleportEntityPacket.class, "c");
    private static final FastFieldAccessor y = FastFieldAccessor.forClassField(ClientboundTeleportEntityPacket.class, "d");
    private static final FastFieldAccessor z = FastFieldAccessor.forClassField(ClientboundTeleportEntityPacket.class, "e");
    private static final FastFieldAccessor yRot = FastFieldAccessor.forClassField(ClientboundTeleportEntityPacket.class, "f");
    private static final FastFieldAccessor xRot = FastFieldAccessor.forClassField(ClientboundTeleportEntityPacket.class, "g");
    private static final FastFieldAccessor onGround = FastFieldAccessor.forClassField(ClientboundTeleportEntityPacket.class, "h");

    public static ClientboundTeleportEntityPacket createNew(PacketEntity entity, Location location) {
        ClientboundTeleportEntityPacket packet = ClassUtils.INSTANCE.newInstance(ClientboundTeleportEntityPacket.class);
        entityId.setInt(packet, entity.getEntityId());
        x.setDouble(packet, location.getX());
        y.setDouble(packet, location.getY());
        z.setDouble(packet, location.getZ());
        yRot.setByte(packet, (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        xRot.setByte(packet, (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        onGround.setBoolean(packet, true);

        return packet;
    }
}
