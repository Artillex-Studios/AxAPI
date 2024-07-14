package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface DebugMarker {

    static DebugMarker create(Location location, String string, Color color, int transparency, int duration) {
        return NMSHandlers.getNmsHandler().marker(color, string, duration, transparency, location);
    }

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
