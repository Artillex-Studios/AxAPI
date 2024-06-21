package com.artillexstudios.axapi.packetentity;

import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PacketEntity {

    void teleport(Location location);

    Location location();

    void meta(EntityMeta meta);

    EntityMeta meta();

    int id();

    void spawn();

    void hide(Player player);

    void show(Player player);

    void setVisibleByDefault(boolean visible);
}
