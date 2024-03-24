package com.artillexstudios.axapi.nms.v1_18_R2.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Title implements com.artillexstudios.axapi.utils.Title {
    private Component title;
    private Component subTitle;
    private int fadeInTime;
    private int stayTime;
    private int fadeOutTime;
    private ClientboundSetTitleTextPacket titleTextPacket;
    private ClientboundSetTitlesAnimationPacket animationPacket;
    private ClientboundSetSubtitleTextPacket subtitleTextPacket;

    public Title(Component title, Component subTitle, int fadeInTime, int stayTime, int fadeOutTime) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTime = fadeOutTime;

        updatePackets();
    }

    @Override
    public void setTitle(@Nullable Component title) {
        this.title = title;
        updatePackets();
    }

    @Override
    public void setSubTitle(@Nullable Component subTitle) {
        this.subTitle = subTitle;
        updatePackets();
    }

    @Override
    public void set(@Nullable Component title, @Nullable Component subTitle) {
        this.title = title;
        this.subTitle = subTitle;
        updatePackets();
    }

    @Override
    public void set(@Nullable Component title, @Nullable Component subTitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeInTime = fadeIn;
        this.stayTime = stay;
        this.fadeOutTime = fadeOut;
        updatePackets();
    }

    @Override
    public void setTimes(int fadeIn, int stay, int fadeOut) {
        this.fadeInTime = fadeIn;
        this.stayTime = stay;
        this.fadeOutTime = fadeOut;
        updatePackets();
    }

    @Override
    public void setFadeInTime(int fadeIn) {
        this.fadeInTime = fadeIn;
        updatePackets();
    }

    @Override
    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
        updatePackets();
    }

    @Override
    public void setFadeOutTime(int fateOut) {
        this.fadeOutTime = fateOut;
        updatePackets();
    }

    @Override
    public void send(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(animationPacket);

        if (titleTextPacket != null) {
            serverPlayer.connection.send(titleTextPacket);
        }

        if (subtitleTextPacket != null) {
            serverPlayer.connection.send(subtitleTextPacket);
        }
    }

    @Override
    public void clear(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        serverPlayer.connection.send(new ClientboundClearTitlesPacket(true));
    }

    private void updatePackets() {
        if (title == null || title == Component.empty()) {
            titleTextPacket = null;
        } else {
            titleTextPacket = new ClientboundSetTitleTextPacket(net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(title)));
        }

        if (subTitle == null || subTitle == Component.empty()) {
            subtitleTextPacket = null;
        } else {
            subtitleTextPacket = new ClientboundSetSubtitleTextPacket(net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(subTitle)));
        }

        animationPacket = new ClientboundSetTitlesAnimationPacket(fadeInTime, stayTime, fadeOutTime);
    }
}
