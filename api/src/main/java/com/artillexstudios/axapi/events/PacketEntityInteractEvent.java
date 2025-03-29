package com.artillexstudios.axapi.events;

import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundInteractWrapper;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.shared.axapi.utils.Vector3f;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PacketEntityInteractEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final PacketEntity packetEntity;
    private final boolean attack;
    private final Vector3f position;
    private final ServerboundInteractWrapper.InteractionHand hand;
    private final Player player;

    public PacketEntityInteractEvent(@NotNull Player player, PacketEntity packetEntity, boolean attack, Vector3f position, ServerboundInteractWrapper.InteractionHand hand) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.packetEntity = packetEntity;
        this.attack = attack;
        this.position = position;
        this.hand = hand;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public PacketEntity getPacketEntity() {
        return packetEntity;
    }

    public boolean isAttack() {
        return attack;
    }

    public Vector3f getPosition() {
        return position;
    }

    public ServerboundInteractWrapper.InteractionHand getHand() {
        return hand;
    }

    public Player getPlayer() {
        return player;
    }
}
