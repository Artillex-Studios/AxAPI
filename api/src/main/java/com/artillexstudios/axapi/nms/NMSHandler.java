package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.selection.BlockSetter;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface NMSHandler {

    byte[] serializeItemStack(ItemStack itemStack);

    ItemStack deserializeItemStack(byte[] bytes);

    void injectPlayer(Player player);

    void uninjectPlayer(Player player);

    int getProtocolVersionId(Player player);

    void setItemStackTexture(ItemMeta meta, String texture);

    String getTextureValue(ItemMeta meta);

    PacketEntityTracker newTracker();

    BlockSetter newSetter(World world);

    String toSNBT(ItemStack itemStack);

    ItemStack fromSNBT(String snbt);

    default ParallelBlockSetter newParallelSetter(World world) {
        return null;
    }
}
