package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.hologram.page.HologramPage;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.Location;

public record HologramType<T>(
        TriFunction<Hologram, Boolean, Location, HologramPage<T, ?>> function) {

    public HologramPage<T, ?> create(Hologram hologram, boolean isFirstPage, Location location) {
        return this.function.apply(hologram, isFirstPage, location);
    }
}
