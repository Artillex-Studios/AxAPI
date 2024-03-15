package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.BossBar;
import com.artillexstudios.axapi.utils.Title;
import net.kyori.adventure.text.Component;
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

    ActionBar newActionBar(Component content);

    Title newTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut);

    BossBar newBossBar(Component title, float progress, BossBar.Color color, BossBar.Style style, BossBar.Flag... flags);

    CompoundTag newTag();

    WrappedItemStack wrapItem(ItemStack itemStack);

    default ParallelBlockSetter newParallelSetter(World world) {
        return null;
    }
}
