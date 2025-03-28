package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.utils.PlayerTextures;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public interface ServerPlayerWrapper extends Wrapper<Player> {

    static ServerPlayerWrapper wrap(Object player) {
        return WrapperRegistry.SERVER_PLAYER.map(player);
    }

    void inject();

    void uninject();

    void sendPacket(Object packet);

    void message(Component message);

    double getBase(Attribute attribute);

    PlayerTextures textures();

    double getX();

    double getZ();
}
