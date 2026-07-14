package com.artillexstudios.axapi.structures;

import org.bukkit.Location;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a structure that is already placed in a world.
 *
 * @param baseLocation The location of the structure, which in terms of y coordinates is on the bottom layer of the structure, in terms of x and z coordinates is
 *                     in the top left corner of the structure.
 * @param structure    The structure that this represents.
 * @param locations    The locations that are the parts of this structure.
 */
public record PlacedStructure(Location baseLocation, Structure structure, List<Location> locations) {

    /**
     * Tries to parse a PlacedStructure from the StructureRegistry based on a coordinate of the structure.
     *
     * @param location The location to try to parse the PlacedStructure from.
     * @return If a structure was found, a PlacedStructure, or null, if no structure was found at that location.
     */
    @Nullable
    static PlacedStructure tryParse(Location location) {
        // Firstly, we need to check if the block at the location is the part of the structure at all.
        for (Structure structure : StructureRegistry.getInstance().structures()) {
            for (ComplexOffset locationOffset : getLocationOffsets(structure, location)) {
                if (locationOffset == null) {
                    continue;
                }

                PlacedStructure placedStructure = getPlacedStructure(structure, location.clone(), locationOffset);
                if (placedStructure == null) {
                    continue;
                }

                return placedStructure;
            }
        }

        return null;
    }

    @NonNull
    private static List<ComplexOffset> getLocationOffsets(Structure structure, Location location) {
        final List<ComplexOffset> offsets = new ArrayList<>();
        for (StructureLayer layer : structure.layers()) {
            for (StructurePosition position : layer.positions()) {
                if (position.isSameAs(location)) {
                    offsets.add(new ComplexOffset(position.offsetX(), layer.offsetY(), position.offsetZ()));
                }
            }
        }

        return offsets;
    }

    // Since the structure can be oriented in any 4 of the cardinal directions, we need to check in all of them.
    // Cases: 1. x is x and z is z
    //        2. x is -z and z is x
    //        3. x is -x and z is -z
    //        4. z is -x and x is z
    @Nullable
    private static PlacedStructure getPlacedStructure(Structure structure, Location location, ComplexOffset offset) {
        StructureLayer baseLayer = structure.getLayerByOffset(0);
        if (baseLayer == null) {
            return null;
        }

        StructurePosition position = baseLayer.getCornerPosition();
        if (position == null) {
            return null;
        }

        List<Location> locations = new ArrayList<>();

        out:
        for (FacingDirection direction : FacingDirection.values()) {
            Location newLocation = offset.getRelativeLocation2D(location, direction);
            if (!position.isSameAs(newLocation)) {
                continue;
            }

            locations.clear();
            if (!collectLayerLocations(newLocation.clone(), baseLayer, direction, locations)) {
                continue;
            }

            for (int i = 1; i <= structure.maxOffsetY(); i++) {
                StructureLayer layerByOffset = structure.getLayerByOffset(i);
                if (layerByOffset == null) {
                    throw new IllegalStateException("The constructed StructureLayer does not contain a mapping for all y layers.");
                }

                if (!collectLayerLocations(newLocation, layerByOffset, direction, locations)) {
                    continue out;
                }
            }

            return new PlacedStructure(newLocation, structure, new ArrayList<>(locations));
        }

        return null;
    }

    private static boolean collectLayerLocations(Location cornerLocation, StructureLayer layer, FacingDirection direction, List<Location> output) {
        int maxOffsetX = layer.maxOffsetX();
        int maxOffsetZ = layer.maxOffsetZ();
        for (int i = 0; i <= maxOffsetX; i++) {
            for (int j = 0; j <= maxOffsetZ; j++) {
                ComplexOffset offset = direction.getOffset(i, j);
                Location location = cornerLocation.clone().add(offset.offsetX(), 0, offset.offsetZ());
                StructurePosition position = layer.getByOffset(i, j);
                if (position == null) {
                    throw new IllegalStateException("The constructed StructureLayer does not contain a mapping for all x and z offsets.");
                }

                if (!position.isSameAs(location)) {
                    return false;
                }

                output.add(location);
            }
        }

        return true;
    }
}
