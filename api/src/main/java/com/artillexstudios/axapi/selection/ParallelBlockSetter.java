package com.artillexstudios.axapi.selection;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.bukkit.block.data.BlockData;

import java.util.function.IntConsumer;

public interface ParallelBlockSetter {

    void fill(Cuboid cuboid, EnumeratedDistribution<BlockData> distribution, IntConsumer consumer);
}
