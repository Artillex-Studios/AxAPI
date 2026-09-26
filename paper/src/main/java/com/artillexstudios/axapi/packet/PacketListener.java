package com.artillexstudios.axapi.packet;

import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class PacketListener {

    public void onPacketSending(PacketEvent event) {
    }

    public void onPacketReceive(PacketEvent event) {
    }

    @Nullable
    public List<PacketType> getListeningTo() {
        return null;
    }
}
