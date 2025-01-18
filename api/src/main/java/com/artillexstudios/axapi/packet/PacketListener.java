package com.artillexstudios.axapi.packet;

public abstract class PacketListener {

    public abstract void onPacketSending(PacketEvent event);

    public abstract void onPacketReceive(PacketEvent event);
}
