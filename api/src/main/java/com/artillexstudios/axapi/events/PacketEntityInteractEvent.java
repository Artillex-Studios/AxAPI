package com.artillexstudios.axapi.events;

import com.artillexstudios.axapi.entity.impl.PacketEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class PacketEntityInteractEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final PacketEntity packetEntity;
    private final boolean attack;
    private final Vector position;
    private final EquipmentSlot hand;
    private final Player player;

    public PacketEntityInteractEvent(@NotNull Player player, PacketEntity packetEntity, boolean attack, Vector position, EquipmentSlot hand) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.packetEntity = packetEntity;
        this.attack = attack;
        this.position = position;
        this.hand = hand;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public PacketEntity getPacketEntity() {
        return packetEntity;
    }

    public boolean isAttack() {
        return attack;
    }

    public Vector getPosition() {
        return position;
    }

    public EquipmentSlot getHand() {
        return hand;
    }

    public Player getPlayer() {
        return player;
    }
}
