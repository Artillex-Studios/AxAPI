package com.artillexstudios.axapi.structures;

import org.bukkit.Location;
import org.jspecify.annotations.NonNull;

public record ComplexOffset(int offsetX, int offsetY, int offsetZ) {

    @NonNull
    public Location getRelativeLocation2D(@NonNull Location location, FacingDirection direction) {
        ComplexOffset offset = direction.getOffset(this.offsetX, this.offsetZ);
        return location.clone().add(offset.offsetX(), 0, offset.offsetZ());
    }
}
