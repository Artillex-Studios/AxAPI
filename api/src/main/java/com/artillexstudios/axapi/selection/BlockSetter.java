package com.artillexstudios.axapi.selection;

import org.bukkit.block.data.BlockData;

public interface BlockSetter {

    void setBlock(int x, int y, int z, BlockData data);

    void finalise();
}
