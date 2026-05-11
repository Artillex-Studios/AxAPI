package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketSide;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public interface ServerWrapper extends Wrapper<Server> {
    ServerWrapper INSTANCE = WrapperRegistry.SERVER.map(null);

    int nextEntityId();

    OfflinePlayer getCachedOfflinePlayer(String name);

    Object transformPacket(FriendlyByteBuf buf);

    String listPackets(PacketSide side);
}
