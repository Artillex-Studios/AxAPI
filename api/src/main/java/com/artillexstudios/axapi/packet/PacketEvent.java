package com.artillexstudios.axapi.packet;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class PacketEvent {
    private final Player player;
    private final PacketType type;
    private final PacketSide side;
    private final Supplier<FriendlyByteBuf> in;
    private final Supplier<FriendlyByteBuf> out;
    private FriendlyByteBuf outBuf;
    private FriendlyByteBuf inBuf;
    private boolean cancelled = false;
    private boolean handled = false;

    public PacketEvent(Player player, PacketSide side, PacketType type, Supplier<FriendlyByteBuf> in, Supplier<FriendlyByteBuf> out) {
        this.player = player;
        this.side = side;
        this.type = type;
        this.in = in;
        this.out = out;
    }

    public Player player() {
        return this.player;
    }

    public int id() {
        return this.id;
    }

    public PacketSide side() {
        return this.side;
    }

    public FriendlyByteBuf in() {
        if (this.inBuf == null) {
            FriendlyByteBuf returning = this.in.get();
            this.inBuf = returning;
            return returning;
        }

        return this.inBuf;
    }

    public FriendlyByteBuf out() {
        if (this.outBuf == null) {
            FriendlyByteBuf returning = this.out.get();
            this.outBuf = returning;
            return returning;
        }

        return this.outBuf;
    }

    @Nullable
    public FriendlyByteBuf directOut() {
        return this.outBuf;
    }

    public boolean handled() {
        return this.handled;
    }

    public boolean cancelled() {
        return this.cancelled;
    }
}
