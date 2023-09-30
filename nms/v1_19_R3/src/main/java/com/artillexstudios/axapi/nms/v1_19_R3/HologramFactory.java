package com.artillexstudios.axapi.nms.v1_19_R3;

import com.artillexstudios.axapi.hologram.Hologram;
import org.bukkit.Location;

public class HologramFactory implements com.artillexstudios.axapi.hologram.HologramFactory {

    @Override
    public Hologram spawnHologram(Location location, String name, double lineHeight) {
        return new com.artillexstudios.axapi.nms.v1_19_R3.hologram.Hologram(location, name, lineHeight);
    }
}
