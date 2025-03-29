package com.artillexstudios.axapi.nms;

import com.artillexstudios.axapi.gui.AnvilInput;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.items.component.DataComponentImpl;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.loot.LootTable;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.nms.wrapper.WrapperMapper;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.serializers.Serializer;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.BossBar;
import com.artillexstudios.axapi.utils.DebugMarker;
import com.artillexstudios.axapi.utils.Title;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NMSHandler {
    Logger log = LoggerFactory.getLogger(NMSHandler.class);
    String PACKET_HANDLER = "packet_handler";

    Serializer<Object, Component> componentSerializer();

    int getProtocolVersionId(Player player);

    PacketEntity createEntity(EntityType entityType, Location location);

    ActionBar newActionBar(Component content);

    Title newTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut);

    BossBar newBossBar(Component title, float progress, BossBar.Color color, BossBar.Style style, BossBar.Flag... flags);

    CompoundTag newTag();

    void openSignInput(SignInput signInput);

    void setTitle(Inventory inventory, Component title);

    DataComponentImpl dataComponents();

    DebugMarker marker(Color color, String message, int duration, int transparency, Location location);

    ServerPlayerWrapper dummyPlayer();

    LootTable lootTable(Key key);

    void openAnvilInput(AnvilInput anvilInput);

    <T extends WrapperMapper<?>> T mapper(String id);
}
