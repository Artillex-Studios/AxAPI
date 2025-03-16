package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.PlayerTextures;
import org.bukkit.entity.Player;

public interface ServerPlayerWrapper extends Wrapper<Player> {

    static ServerPlayerWrapper wrap(Object player) {
        return NMSHandlers.getNmsHandler().wrapper(player);
    }

    PlayerTextures textures();

    double getX();

    double getZ();

    default void sendPacket(PacketWrapper wrapper) {

    }
}
