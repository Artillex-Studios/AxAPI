package com.artillexstudios.axapi.nms.v1_20_R2.entity;

import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PacketItem extends PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketItem {
    private final HashMap<UUID, ItemStack> itemOverrides = new HashMap<>();
    private ItemStack itemStack;

    public PacketItem(Location location) {
        super(EntityType.DROPPED_ITEM, location);
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();

            serverPlayer.connection.send(new ClientboundSetEntityDataPacket(entityId, dataValues(serverPlayer)));
        }
    }

    @Override
    public void setItemStack(ItemStack itemStack, Player player) {
        if (itemStack == null) {
            itemOverrides.remove(player.getUniqueId());
            return;
        }

        itemOverrides.put(player.getUniqueId(), itemStack);

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(entityId, dataValues(serverPlayer)));
    }

    @Override
    public ItemStack getItemStack(Player player) {
        return itemOverrides.getOrDefault(player.getUniqueId(), itemStack);
    }

    @Override
    public List<SynchedEntityData.DataValue<?>> dataValues(ServerPlayer player) {
        List<SynchedEntityData.DataValue<?>> dataValues = super.dataValues(player);
        dataValues.add(SynchedEntityData.DataValue.create(EntityDataSerializers.ITEM_STACK.createAccessor(8), CraftItemStack.asNMSCopy(getItemStack(player.getBukkitEntity()))));
        return dataValues;
    }

    @Override
    public void clear(Player player) {
        super.clear(player);
        itemOverrides.remove(player.getUniqueId());
    }
}
