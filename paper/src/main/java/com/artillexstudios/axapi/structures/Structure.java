package com.artillexstudios.axapi.structures;

import com.artillexstudios.axapi.utils.MathUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public record Structure(String id, List<StructureLayer> layers, Consumer<PlayerInteractEvent> interactListener,
                        Consumer<Player> breakListener) {

    @Nullable
    public StructureLayer getLayerByOffset(int offset) {
        for (StructureLayer layer : this.layers) {
            if (layer.offsetY() == offset) {
                return layer;
            }
        }

        return null;
    }

    public int maxOffsetY() {
        return MathUtils.findLargestValue(this.layers, StructureLayer::offsetY);
    }

    @Nullable
    public StructurePosition getByComplexOffset(ComplexOffset offset) {
        StructureLayer layerByOffset = this.getLayerByOffset(offset.offsetY());
        if (layerByOffset == null) {
            return null;
        }

        return layerByOffset.getByOffset(offset.offsetX(), offset.offsetZ());
    }
}
