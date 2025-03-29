package com.artillexstudios.axapi.selection;

import com.artillexstudios.axapi.nms.wrapper.WorldWrapper;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;

public class BlockPlacer {
    private final Cuboid selection;
    private final EnumeratedDistribution<BlockData> distribution;

    public BlockPlacer(Cuboid selection, EnumeratedDistribution<Material> preDistribution) {
        this.selection = selection;

        List<Pair<BlockData, Double>> distribution = new ArrayList<>(preDistribution.getPmf().size());
        preDistribution.getPmf().forEach((pair) -> distribution.add(Pair.create(Bukkit.createBlockData(pair.getFirst()), pair.getSecond())));
        this.distribution = new EnumeratedDistribution<>(distribution);
    }

    public int run() {
        BlockSetter setter = WorldWrapper.wrap(this.selection.getWorld()).setter();
        int blockCount = 0;
        int chunkMinX = this.selection.getMinX() >> 4;
        int chunkMaxX = this.selection.getMaxX() >> 4;
        int chunkMinZ = this.selection.getMinZ() >> 4;
        int chunkMaxZ = this.selection.getMaxZ() >> 4;

        for (int chunkX = chunkMinX; chunkX <= chunkMaxX; chunkX++) {
            int minX = Math.max(this.selection.getMinX(), chunkX << 4);
            int maxX = Math.min(this.selection.getMaxX(), (chunkX << 4) + 15);

            for (int chunkZ = chunkMinZ; chunkZ <= chunkMaxZ; chunkZ++) {
                int minZ = Math.max(this.selection.getMinZ(), chunkZ << 4);
                int maxZ = Math.min(this.selection.getMaxZ(), (chunkZ << 4) + 15);

                for (int y = this.selection.getMinY(); y <= this.selection.getMaxY(); y++) {
                    for (int x = minX; x <= maxX; x++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            BlockData type = this.distribution.sample();

                            blockCount++;
                            setter.setBlock(x, y, z, type);
                        }
                    }
                }
            }
        }

        setter.finalise();
        return blockCount;
    }
}
