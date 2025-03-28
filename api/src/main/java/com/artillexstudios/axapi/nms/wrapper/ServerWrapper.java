package com.artillexstudios.axapi.nms.wrapper;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public interface ServerWrapper extends Wrapper<Server> {

    int nextEntityId();

    OfflinePlayer getCachedOfflinePlayer(String name);
}
