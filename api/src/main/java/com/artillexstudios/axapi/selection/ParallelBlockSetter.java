package com.artillexstudios.axapi.selection;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.bukkit.block.data.BlockData;

public interface ParallelBlockSetter {

    int fill(Cuboid cuboid, EnumeratedDistribution<BlockData> distribution);
}
