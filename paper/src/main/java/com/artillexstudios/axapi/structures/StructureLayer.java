package com.artillexstudios.axapi.structures;

import com.artillexstudios.axapi.utils.MathUtils;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Represents a layer of the structure, in a 3D plane. This is based on the y coordinates.
 * @param parent The parent structure that this layer is part of.
 * @param offsetY The offset of y coordinates compared to the y coordinate of the bottom layer of the structure in a 3D plane.
 * @param positions The list of positions that this layer consists of, they contain the x and z offsets.
 */
public record StructureLayer(Structure parent, int offsetY, List<StructurePosition> positions) {

    @Nullable
    public StructurePosition getCornerPosition() {
        return this.getByOffset(0, 0);
    }

    @Nullable
    public StructurePosition getByOffset(int offsetX, int offsetZ) {
        for (StructurePosition position : this.positions) {
            if (position.offsetX() == offsetX && position.offsetZ() == offsetZ) {
                return position;
            }
        }

        return null;
    }

    public int maxOffsetX() {
        return MathUtils.findLargestValue(this.positions, StructurePosition::offsetX);
    }

    public int maxOffsetZ() {
        return MathUtils.findLargestValue(this.positions, StructurePosition::offsetZ);
    }
}
