package com.artillexstudios.axapi.nms.v1_21_R4;

import com.artillexstudios.axapi.gui.AnvilInput;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponentImpl;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.loot.LootTable;
import com.artillexstudios.axapi.nms.v1_21_R4.wrapper.WrapperMapperRegistry;
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
import org.bukkit.inventory.ItemStack;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {

    @Override
    public Serializer<Object, Component> componentSerializer() {
        return null;
    }

    @Override
    public int getProtocolVersionId(Player player) {
        return 0;
    }

    @Override
    public PacketEntity createEntity(EntityType entityType, Location location) {
        return null;
    }

    @Override
    public ActionBar newActionBar(Component content) {
        return null;
    }

    @Override
    public Title newTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        return null;
    }

    @Override
    public BossBar newBossBar(Component title, float progress, BossBar.Color color, BossBar.Style style, BossBar.Flag... flags) {
        return null;
    }

    @Override
    public CompoundTag newTag() {
        return null;
    }

    @Override
    public WrappedItemStack wrapItem(ItemStack itemStack) {
        return null;
    }

    @Override
    public WrappedItemStack wrapItem(String snbt) {
        return null;
    }

    @Override
    public WrappedItemStack wrapItem(byte[] bytes) {
        return null;
    }

    @Override
    public void openSignInput(SignInput signInput) {

    }

    @Override
    public void setTitle(Inventory inventory, Component title) {

    }

    @Override
    public DataComponentImpl dataComponents() {
        return null;
    }

    @Override
    public DebugMarker marker(Color color, String message, int duration, int transparency, Location location) {
        return null;
    }

    @Override
    public ServerPlayerWrapper dummyPlayer() {
        return null;
    }

    @Override
    public LootTable lootTable(Key key) {
        return null;
    }

    @Override
    public void openAnvilInput(AnvilInput anvilInput) {

    }

    @Override
    public ServerPlayerWrapper wrapper(Object player) {
        return null;
    }

    @Override
    public <T extends WrapperMapper<?>> T mapper(String id) {
        return WrapperMapperRegistry.mapper(id);
    }
}
