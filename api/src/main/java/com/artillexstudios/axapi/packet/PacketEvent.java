package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
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
    private PacketWrapper wrapper;
    private Integer readerIndex;
    private Integer writerIndex;
    private boolean cancelled = false;

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

    public PacketType type() {
        return this.type;
    }

    public PacketSide side() {
        return this.side;
    }

    public FriendlyByteBuf in() {
        if (this.inBuf == null) {
            FriendlyByteBuf returning = this.in.get();
            this.inBuf = returning;
            this.readerIndex = returning.readerIndex();
            return returning;
        }

        return this.inBuf;
    }

    public FriendlyByteBuf out() {
        if (this.outBuf == null) {
            FriendlyByteBuf returning = this.out.get();
            this.outBuf = returning;
            this.writerIndex = returning.writerIndex();
            return returning;
        }

        return this.outBuf;
    }

    public void setWrapper(PacketWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public PacketWrapper wrapper() {
        return this.wrapper;
    }

    @Nullable
    public FriendlyByteBuf directOut() {
        return this.outBuf;
    }

    @Nullable
    public FriendlyByteBuf directIn() {
        return this.inBuf;
    }

    @Nullable
    public Integer readerIndex() {
        return this.readerIndex;
    }

    public Integer writerIndex() {
        return this.writerIndex;
    }

    public void cancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean cancelled() {
        return this.cancelled;
    }
}
