package com.artillexstudios.axapi.selection;

import com.artillexstudios.axapi.nms.NMSHandlers;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;

public class BlockPlacer {
    private final boolean killed = false;
    private final Cuboid selection;
    private final EnumeratedDistribution<BlockData> distribution;

    public BlockPlacer(Cuboid selection, EnumeratedDistribution<Material> preDistribution) {
        this.selection = selection;

        List<Pair<BlockData, Double>> distribution = new ArrayList<>(preDistribution.getPmf().size());
        preDistribution.getPmf().forEach((pair) -> distribution.add(Pair.create(Bukkit.createBlockData(pair.getFirst()), pair.getSecond())));
        this.distribution = new EnumeratedDistribution<>(distribution);
    }

    public int run() {
        BlockSetter setter = NMSHandlers.getNmsHandler().newSetter(selection.getWorld());
        int blockCount = 0;
        int chunkMinX = selection.getMinX() >> 4;
        int chunkMaxX = selection.getMaxX() >> 4;
        int chunkMinZ = selection.getMinZ() >> 4;
        int chunkMaxZ = selection.getMaxZ() >> 4;
        
        for (int chunkX = chunkMinX; chunkX <= chunkMaxX; chunkX++) {
            int minX = Math.max(selection.getMinX(), chunkX << 4);
            int maxX = Math.min(selection.getMaxX(), (chunkX << 4) + 15);

            for (int chunkZ = chunkMinZ; chunkZ <= chunkMaxZ; chunkZ++) {
                int minZ = Math.max(selection.getMinZ(), chunkZ << 4);
                int maxZ = Math.min(selection.getMaxZ(), (chunkZ << 4) + 15);
                
                for (int y = selection.getMinY(); y <= selection.getMaxY(); y++) {
                    for (int x = minX; x <= maxX; x++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            BlockData type = distribution.sample();
                    
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
