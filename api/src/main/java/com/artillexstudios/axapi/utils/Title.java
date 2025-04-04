package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetSubtitleTextWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetTitleTextWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetTitlesAnimationWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public final class Title {
    private Component title;
    private Component subTitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;
    private ClientboundSetTitleTextWrapper titleTextPacket;
    private ClientboundSetTitlesAnimationWrapper animationPacket;
    private ClientboundSetSubtitleTextWrapper subtitleTextPacket;

    public Title(Component title, Component subTitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public static Title create(Component title) {
        return new Title(title, null, 10, 70, 20);
    }

    public static Title create(Component title, Component subTitle) {
        return new Title(title, subTitle, 10, 70, 20);
    }

    public static Title create(Component title, Component subTitle, int fadeIn, int stay, int fadeOut) {
        return new Title(title, subTitle, fadeIn, stay, fadeOut);
    }

    public static Title create(Component subTitle, int fadeIn, int stay, int fadeOut) {
        return new Title(null, subTitle, fadeIn, stay, fadeOut);
    }

    public static void send(Player player, Component title, Component subTitle) {
        Title titleObject = create(title, subTitle);
        titleObject.send(player);
    }

    public static void sendTitle(Player player, Component title) {
        Title titleObject = create(title);
        titleObject.send(player);
    }

    public static void sendSubTitle(Player player, Component subTitle) {
        Title titleObject = create(null, subTitle);
        titleObject.send(player);
    }

    public void setTitle(@Nullable Component title) {

    }

    public void setSubTitle(@Nullable Component subTitle) {

    }

    public void set(@Nullable Component title, @Nullable Component subTitle) {

    }

    public void set(@Nullable Component title, @Nullable Component subTitle, int fadeIn, int stay, int fadeOut) {

    }

    public void setTimes(int fadeIn, int stay, int fadeOut) {

    }

    public void setFadeIn(int fadeIn) {

    }

    public void setStay(int stay) {

    }

    public void setFadeOut(int fateOut) {

    }

    public void send(Player player) {

    }

    public void clear(Player player) {

    }

    private void updatePacket() {
        if (this.title == null || this.title == Component.empty()) {
            this.titleTextPacket = null;
        } else {
            this.titleTextPacket = new ClientboundSetTitleTextWrapper(this.title);
        }

        if (this.subTitle == null || this.subTitle == Component.empty()) {
            this.subtitleTextPacket = null;
        } else {
            this.subtitleTextPacket = new ClientboundSetSubtitleTextWrapper(this.subTitle);
        }

        this.animationPacket = new ClientboundSetTitlesAnimationWrapper(this.fadeIn, this.stay, this.fadeOut);
    }
}
