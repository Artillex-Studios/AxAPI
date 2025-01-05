package com.artillexstudios.axapi.packet;

import org.bukkit.entity.Player;

public final class PacketEvent {
    private final Player player;
    private final FriendlyByteBuf buf;
    private boolean cancelled = false;
    private boolean handled = false;

    public PacketEvent(Player player, FriendlyByteBuf buf, FriendlyByteBuf out) {
        this.player = player;
        this.buf = buf;
    }

    public Player player() {
        return this.player;
    }

    public FriendlyByteBuf buf() {
        return this.buf;
    }

    public boolean handled() {
        return this.handled;
    }

    public boolean cancelled() {
        return this.cancelled;
    }
}
