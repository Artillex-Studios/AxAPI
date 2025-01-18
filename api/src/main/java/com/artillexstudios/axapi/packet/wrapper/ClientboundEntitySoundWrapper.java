package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.packet.PacketEvent;

public final class ClientboundEntitySoundWrapper {

    public static boolean check(PacketEvent event) {
        return event.id() == 0x6E;
    }
}
