package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.serializers.Serializer;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.BossBar;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.Title;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NMSHandler {
    Logger log = LoggerFactory.getLogger(NMSHandler.class);
    String PACKET_HANDLER = "packet_handler";

    Serializer<Object, Component> componentSerializer();

    void injectPlayer(Player player);

    void uninjectPlayer(Player player);

    int getProtocolVersionId(Player player);

    PacketEntityTracker newTracker();

    BlockSetter newSetter(World world);

    ActionBar newActionBar(Component content);

    Title newTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut);

    BossBar newBossBar(Component title, float progress, BossBar.Color color, BossBar.Style style, BossBar.Flag... flags);

    CompoundTag newTag();

    WrappedItemStack wrapItem(ItemStack itemStack);

    WrappedItemStack wrapItem(String snbt);

    WrappedItemStack wrapItem(byte[] bytes);

    void openSignInput(SignInput signInput);

    void setTitle(Inventory inventory, Component title);

    default ParallelBlockSetter newParallelSetter(World world) {
        return null;
    }
}
