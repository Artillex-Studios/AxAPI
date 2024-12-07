package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.entity.Player;

public interface ServerPlayerWrapper extends Wrapper<Player> {

    static ServerPlayerWrapper wrap(Player player) {
        return NMSHandlers.getNmsHandler().wrapper(player);
    }

    double getX();

    double getZ();
}
