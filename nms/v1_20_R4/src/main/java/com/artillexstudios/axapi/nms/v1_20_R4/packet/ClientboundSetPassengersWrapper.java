package com.artillexstudios.axapi.nms.v1_20_R4.packet;

import com.artillexstudios.axapi.utils.ClassUtils;
import com.artillexstudios.axapi.utils.FastFieldAccessor;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;

public class ClientboundSetPassengersWrapper {
    private static final FastFieldAccessor entityId = FastFieldAccessor.forClassField(ClientboundSetPassengersPacket.class, "b");
    private static final FastFieldAccessor passengers = FastFieldAccessor.forClassField(ClientboundSetPassengersPacket.class, "c");

    public static ClientboundSetPassengersPacket createNew(int ridingEntity, int[] passengerIds) {
        ClientboundSetPassengersPacket packet = ClassUtils.INSTANCE.newInstance(ClientboundSetPassengersPacket.class);
        entityId.setInt(packet, ridingEntity);
        passengers.set(packet, passengerIds);

        return packet;
    }
}
