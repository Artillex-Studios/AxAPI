package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.Location;

public interface HologramFactory {
    double LINE_SPACE = 0.75;

    static HologramFactory get() {
        return NMSHandlers.getHologramFactory();
    }

    Hologram spawnHologram(Location location, String name, double lineHeight);

    default Hologram spawnHologram(Location location, String name) {
        return spawnHologram(location, name, LINE_SPACE);
    }
}
