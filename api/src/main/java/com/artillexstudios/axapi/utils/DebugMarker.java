package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundCustomPayloadWrapper;
import com.artillexstudios.axapi.utils.position.BlockPosition;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class DebugMarker {
    private static final Key DEBUG_GAME_TEST_ADD_MARKER = Key.key("debug/game_test_add_marker");
    private Color color;
    private String message;
    private int duration;
    private int transparency;
    private Location location;
    private ClientboundCustomPayloadWrapper packet;

    public DebugMarker(Color color, String message, int duration, int transparency, Location location) {
        this.color = color;
        this.message = message;
        this.duration = duration;
        this.transparency = transparency;
        this.location = location;

        this.updatePacket();
    }

    static DebugMarker create(Location location, String message, Color color, int transparency, int duration) {
        return new DebugMarker(color, message, duration, transparency, location);
    }

    public void color(Color color) {
        this.color = color;
        this.updatePacket();
    }

    public Color color() {
        return this.color;
    }

    public void message(String message) {
        this.message = message;
        this.updatePacket();
    }

    public String message() {
        return this.message;
    }

    public void duration(int duration) {
        this.duration = duration;
        this.updatePacket();
    }

    public int duration() {
        return this.duration;
    }

    public void transparency(int transparency) {
        this.transparency = transparency;
        this.updatePacket();
    }

    public int transparency() {
        return this.transparency;
    }

    public void location(Location location) {
        this.location = location.clone();
        this.updatePacket();
    }

    public Location location() {
        return this.location;
    }

    public void send(Player player) {
        ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
        wrapper.sendPacket(this.packet);
    }

    private void updatePacket() {
        if (this.packet != null) {
            this.packet.data().release();
        }

        FriendlyByteBuf buf = FriendlyByteBuf.alloc();
        buf.writeBlockPos(new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ()));
        buf.writeInt(this.transparency << 24 | this.color.asRGB());
        buf.writeUTF(this.message);
        buf.writeInt(this.duration);

        this.packet = new ClientboundCustomPayloadWrapper(DebugMarker.DEBUG_GAME_TEST_ADD_MARKER, buf);
        buf.release();
    }
}
