package com.artillexstudios.axapi.nms.v1_19_R1.wrapper;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class ServerPlayerWrapper implements com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper {
    private Player wrapped;
    private ServerPlayer serverPlayer;

    public ServerPlayerWrapper(Player player) {
        this.wrapped = player;
    }

    public ServerPlayerWrapper(ServerPlayer player) {
        this.serverPlayer = player;
    }

    @Override
    public double getX() {
        this.update();
        return this.serverPlayer.getX();
    }

    @Override
    public double getZ() {
        this.update();
        return this.serverPlayer.getZ();
    }

    @Override
    public Player wrapped() {
        if (this.wrapped == null) {
            this.wrapped = this.serverPlayer.getBukkitEntity();
        }

        return this.wrapped;
    }

    @Override
    public void update(boolean force) {
        if (this.serverPlayer == null || force) {
            this.serverPlayer = ((CraftPlayer) this.wrapped).getHandle();
        }
    }

    @Override
    public Object asMinecraft() {
        this.update();
        return this.serverPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ServerPlayerWrapper that)) {
            return false;
        }

        return Objects.equals(this.wrapped, that.wrapped);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.wrapped);
    }
}