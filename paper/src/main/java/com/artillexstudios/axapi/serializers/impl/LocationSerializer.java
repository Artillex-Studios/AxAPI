package com.artillexstudios.axapi.serializers.impl;

import com.artillexstudios.axapi.serializers.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

public class LocationSerializer implements Serializer<Location, String> {
    private static final Pattern SPLIT_PATTERN = Pattern.compile(";");

    @Override
    public String serialize(Location object) {
        return object.getWorld().getName() + ';' + object.getX() + ';' + object.getY() + ';' + object.getZ() + ';' + object.getYaw() + ';' + object.getPitch();
    }

    @Override
    public Location deserialize(String value) {
        String[] split = SPLIT_PATTERN.split(value);
        return new Location(Bukkit.getWorld(split[0]), parseDouble(split[1]), parseDouble(split[2]), parseDouble(split[3]), parseFloat(split[4]), parseFloat(split[5]));
    }
}
