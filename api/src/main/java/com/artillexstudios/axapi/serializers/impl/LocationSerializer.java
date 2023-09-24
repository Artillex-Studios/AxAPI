package com.artillexstudios.axapi.serializers.impl;

import com.artillexstudios.axapi.serializers.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

public class LocationSerializer implements Serializer<Location> {

    @Override
    public String serialize(Location object) {
        return String.format("%s;%s;%s;%s;%s;%s", object.getWorld().getName(), object.getX(), object.getY(), object.getZ(), object.getYaw(), object.getPitch());
    }

    @Override
    public Location deserialize(String value) {
        String[] split = value.split(";");
        return new Location(Bukkit.getWorld(split[0]), parseDouble(split[1]), parseDouble(split[2]), parseDouble(split[3]), parseFloat(split[4]), parseFloat(split[5]));
    }
}
