package com.artillexstudios.axapi.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PacketEntity {

    void setName(Component name, Player player);

    Component getName();

    void setName(Component name);

    Component getName(Player player);

    void teleport(Location location);

    void teleport(Location location, Player player);

    Location getLocation();

    Location getLocation(Player player);

    int getViewDistance();

    void setViewDistance(int blocks);

    void show(Player player);

    void hide(Player player);
}
