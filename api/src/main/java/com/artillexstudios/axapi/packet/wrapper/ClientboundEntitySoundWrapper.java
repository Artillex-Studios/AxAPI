package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketTypes;

public final class ClientboundEntitySoundWrapper {

    public static boolean check(PacketEvent event) {
        return event.type() == PacketTypes.ENTITY_SOUND;
    }
}
