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
    private final Selection3D selection;
    private final EnumeratedDistribution<BlockData> distribution;

    public BlockPlacer(Selection3D selection, EnumeratedDistribution<Material> preDistribution) {
//        Thread thread = new Thread(this, "AxAPI-Worker");
        this.selection = selection;

        List<Pair<BlockData, Double>> distribution = new ArrayList<>(preDistribution.getPmf().size());
        preDistribution.getPmf().forEach((pair) -> distribution.add(Pair.create(Bukkit.createBlockData(pair.getFirst()), pair.getSecond())));
        this.distribution = new EnumeratedDistribution<>(distribution);
//        thread.start();
        run();
    }

    public void run() {
        var setter = NMSHandlers.getNmsHandler().newSetter(selection.getWorld());

        for (int x = selection.getMinX(); x < selection.getMaxX(); x++) {
            for (int y = selection.getMinY(); y < selection.getMaxY(); y++) {
                for (int z = selection.getMinZ(); z < selection.getMaxZ(); z++) {
                    var type = distribution.sample();

                    setter.setBlock(x, y, z, type);
                }
            }
        }

        setter.finalise();
    }
}
