package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.items.HashGenerator;
import com.artillexstudios.axapi.utils.PlayerTextures;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Locale;

public interface ServerPlayerWrapper extends Wrapper<Player> {
    String PACKET_HANDLER = "packet_handler";
    String AXAPI_HANDLER = "axapi_handler_" + AxPlugin.getPlugin(AxPlugin.class).getName().toLowerCase(Locale.ENGLISH);

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

    HashGenerator hashGenerator();
}
