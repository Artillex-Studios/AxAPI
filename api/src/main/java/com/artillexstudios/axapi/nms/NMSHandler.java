package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponentImpl;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.serializers.Serializer;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.BossBar;
import com.artillexstudios.axapi.utils.DebugMarker;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.Title;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NMSHandler {
    Logger log = LoggerFactory.getLogger(NMSHandler.class);
    String PACKET_HANDLER = "packet_handler";

    Serializer<Object, Component> componentSerializer();

    void injectPlayer(Player player);

    void uninjectPlayer(Player player);

    int getProtocolVersionId(Player player);

    PacketEntity createEntity(EntityType entityType, Location location);

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

    DataComponentImpl dataComponents();

    OfflinePlayer getCachedOfflinePlayer(String name);

    void sendPacket(Player player, Object packet);

    default ParallelBlockSetter newParallelSetter(World world) {
        return null;
    }

    int nextEntityId();

    Pair<String, String> textures(Player player);

    void sendMessage(Player player, Component message);

    DebugMarker marker(Color color, String message, int duration, int transparency, Location location);

    double getBase(Player player, Attribute attribute);
}
