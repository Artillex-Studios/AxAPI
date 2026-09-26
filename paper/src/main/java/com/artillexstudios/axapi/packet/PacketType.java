package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.utils.Version;

public record PacketType(String name, Version from, Version to, PacketSide packetSide) {

    public static PacketType create(String name, Version from, Version to, PacketSide packetSide) {
        return new PacketType(name, from, to, packetSide);
    }

    public static PacketType createClientbound(String name, Version from, Version to) {
        return new PacketType(name, from, to, PacketSide.CLIENT_BOUND);
    }

    public static PacketType createServerbound(String name, Version from, Version to) {
        return new PacketType(name, from, to, PacketSide.SERVER_BOUND);
    }
}
