package com.artillexstudios.axapi.nms.v1_21_R1.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class DebugMarker implements com.artillexstudios.axapi.utils.DebugMarker {
    private Color color;
    private String message;
    private int duration;
    private int transparency;
    private Location location;
    private ClientboundCustomPayloadPacket packet;

    public DebugMarker(Color color, String message, int duration, int transparency, Location location) {
        this.color = color;
        this.message = message;
        this.duration = duration;
        this.transparency = transparency;
        this.location = location;
        this.createPacket();
    }

    @Override
    public void color(Color color) {
        this.color = color;
        this.createPacket();
    }

    @Override
    public Color color() {
        return this.color;
    }

    @Override
    public void message(String message) {
        this.message = message;
        this.createPacket();
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public void duration(int duration) {
        this.duration = duration;
        this.createPacket();
    }

    @Override
    public int duration() {
        return this.duration;
    }

    @Override
    public void transparency(int transparency) {
        this.transparency = transparency;
        this.createPacket();
    }

    @Override
    public int transparency() {
        return this.transparency;
    }

    @Override
    public void location(Location location) {
        this.location = location;
        this.createPacket();
    }

    @Override
    public Location location() {
        return this.location;
    }

    private void createPacket() {
        this.packet = new ClientboundCustomPayloadPacket(new GameTestAddMarkerDebugPayload(new BlockPos(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ()), this.transparency << 24 | color.asRGB(), message, duration));
    }

    @Override
    public void send(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        serverPlayer.connection.send(this.packet);
    }
}
