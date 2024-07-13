package com.artillexstudios.axapi.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface DebugMarker {

    void color(Color color);

    Color color();

    void message(String message);

    String message();

    void duration(int duration);

    int duration();

    void transparency(int transparency);

    int transparency();

    void location(Location location);

    Location location();

    void send(Player player);
}
