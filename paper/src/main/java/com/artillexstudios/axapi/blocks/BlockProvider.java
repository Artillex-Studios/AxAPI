package com.artillexstudios.axapi.blocks;

import org.bukkit.Location;
import org.jspecify.annotations.NonNull;

public interface BlockProvider {
    BlockProvider VANILLA = new VanillaBlockProvider();

    default boolean compareBlock(@NonNull Location location, @NonNull String id) {
        return this.getComparableBlock(location).equalsIgnoreCase(id);
    }

    @NonNull
    String getComparableBlock(@NonNull Location location);

    void placeBlock(@NonNull Location location, @NonNull String id);
}
