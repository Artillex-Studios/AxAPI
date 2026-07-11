package com.artillexstudios.axapi.structures;

import com.artillexstudios.axapi.blocks.BlockProvider;
import org.bukkit.Location;

/**
 * Represents a position in a structure.
 * @param parent The parent layer that this position is part of.
 * @param offsetX The offset of the x coordinate, relative to the top left corner of the structure in a 2D plane.
 * @param offsetZ The offset of the z coordinate, relative to the top left corner of the structure in a 2D plane.
 * @param provider The BlockProvider to use to check the identifier.
 * @param identifier The identifier of the type of block.
 */
public record StructurePosition(StructureLayer parent, int offsetX, int offsetZ, BlockProvider provider, String identifier) {

    public boolean isSameAs(Location location) {
        return this.provider.compareBlock(location, this.identifier);
    }
}
