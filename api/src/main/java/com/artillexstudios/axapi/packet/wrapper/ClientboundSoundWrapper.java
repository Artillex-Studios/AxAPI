package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.packet.PacketEvent;

public final class ClientboundSoundWrapper {

    public static boolean check(PacketEvent event) {
        return event.id() == 0x6F;
    }
}
