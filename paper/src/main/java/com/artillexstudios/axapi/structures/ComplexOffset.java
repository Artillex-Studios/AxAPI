package com.artillexstudios.axapi.structures;

import org.bukkit.Location;
import org.jspecify.annotations.NonNull;

public record ComplexOffset(int offsetX, int offsetY, int offsetZ) {

    @NonNull
    public Location getRelativeLocation2D(@NonNull Location location, int xQuarter, int zQuarter) {
        return location.clone().add(xQuarter * this.offsetX, 0, zQuarter * this.offsetZ);
    }
}
