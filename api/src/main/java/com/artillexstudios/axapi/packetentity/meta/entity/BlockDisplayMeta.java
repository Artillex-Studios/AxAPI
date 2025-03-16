package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class BlockDisplayMeta extends DisplayMeta {

    public BlockDisplayMeta(Metadata metadata) {
        super(metadata);
    }

    public void blockData(BlockData blockData) {
        this.metadata.set(Accessors.BLOCK_DATA, blockData);
    }

    public BlockData blockData() {
        return this.metadata.get(Accessors.BLOCK_DATA);
    }

    @Override
    protected void defineDefaults() {
        this.metadata.define(Accessors.BLOCK_DATA, Material.AIR.createBlockData());
    }
}
