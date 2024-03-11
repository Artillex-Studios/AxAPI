package com.artillexstudios.axapi.nms.v1_19_R2.utils;

import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/*
        ADD: 0
        REMOVE: 1
        UPDATE_PROGRESS: 2
        UPDATE_NAME: 3
        UPDATE_STYLE: 4
        UPDATE_PROPERTIES: 5
 */
public class BossBar implements com.artillexstudios.axapi.utils.BossBar {
    private final UUID uuid = UUID.randomUUID();
    private final HashSet<Flag> flags = new HashSet<>(4); // We set to 4, since the default strategy is 0.75 and the max we can have is 3.
    private final Set<Player> viewers = Collections.newSetFromMap(new WeakHashMap<>());
    private final ClientboundBossEventPacket removePacket = ClientboundBossEventPacket.createRemovePacket(uuid);
    private Component title;
    private Color color;
    private Style style;
    private float progress;
    private ClientboundBossEventPacket addPacket;

    public BossBar(Component title, float progress, Color color, Style style, Flag... flags) {
        Validate.inclusiveBetween(0.0, 1.0, progress);
        this.title = title;
        this.progress = progress;
        this.color = color;
        this.style = style;
        this.flags.addAll(Arrays.asList(flags));
        updateAddPacket();
    }

    private static int encodeProperties(Set<Flag> flags) {
        boolean darkenSky = flags.contains(Flag.DARKEN_SCREEN);
        boolean dragonMusic = flags.contains(Flag.PLAY_BOSS_MUSIC);
        boolean thickenFog = flags.contains(Flag.CREATE_WORLD_FOG);

        return encodeProperties(darkenSky, dragonMusic, thickenFog);
    }

    // Vanilla code
    private static int encodeProperties(boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
        int i = 0;
        if (darkenSky) {
            i |= 1;
        }

        if (dragonMusic) {
            i |= 2;
        }

        if (thickenFog) {
            i |= 4;
        }

        return i;
    }

    @Override
    public void show(Player player) {
        if (viewers.add(player)) {
            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            serverPlayer.connection.send(addPacket);
        }
    }

    @Override
    public void hide(Player player) {
        if (viewers.remove(player)) {
            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            serverPlayer.connection.send(removePacket);
        }
    }

    @Override
    public void setStyle(Style style) {
        this.style = style;
        updateAddPacket();
        updateStyle();
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        updateAddPacket();
        updateStyle();
    }

    @Override
    public void addFlags(Flag... flag) {
        flags.addAll(Arrays.asList(flag));
        updateAddPacket();
        updateFlags();
    }

    @Override
    public void removeFlags(Flag... flag) {
        Arrays.asList(flag).forEach(flags::remove);
        updateAddPacket();
        updateFlags();
    }

    @Override
    public Set<Flag> getFlags() {
        return Set.copyOf(flags);
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public void setTitle(Component title) {
        this.title = title;
        updateAddPacket();
        updateTitle();
    }

    @Override
    public float getProgress() {
        return this.progress;
    }

    @Override
    public void setProgress(float progress) {
        Validate.inclusiveBetween(0.0, 1.0, progress);
        this.progress = progress;
        updateAddPacket();
        updateProgress();
    }

    @Override
    public void remove() {
        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(removePacket);
        }
    }

    private void updateTitle() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(uuid);
        buf.writeVarInt(3);
        buf.writeComponent(net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(title)));

        ClientboundBossEventPacket titlePacket = new ClientboundBossEventPacket(buf);
        buf.release();

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(titlePacket);
        }
    }

    private void updateProgress() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(uuid);
        buf.writeVarInt(2);
        buf.writeFloat(progress);

        ClientboundBossEventPacket progressPacket = new ClientboundBossEventPacket(buf);
        buf.release();

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(progressPacket);
        }
    }

    private void updateStyle() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(uuid);
        buf.writeVarInt(4);
        buf.writeEnum(BossEvent.BossBarColor.byName(color.getInternalName()));
        buf.writeEnum(BossEvent.BossBarOverlay.byName(style.getInternalName()));

        ClientboundBossEventPacket progressPacket = new ClientboundBossEventPacket(buf);
        buf.release();

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(progressPacket);
        }
    }

    private void updateFlags() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(uuid);
        buf.writeVarInt(5);
        buf.writeByte(encodeProperties(flags));

        ClientboundBossEventPacket progressPacket = new ClientboundBossEventPacket(buf);
        buf.release();

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(progressPacket);
        }
    }

    private void updateAddPacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(uuid);
        buf.writeVarInt(0);
        buf.writeComponent(net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(title)));
        buf.writeFloat(progress);
        buf.writeEnum(BossEvent.BossBarColor.byName(color.getInternalName()));
        buf.writeEnum(BossEvent.BossBarOverlay.byName(style.getInternalName()));
        buf.writeByte(encodeProperties(flags));

        addPacket = new ClientboundBossEventPacket(buf);
        buf.release();
    }
}
