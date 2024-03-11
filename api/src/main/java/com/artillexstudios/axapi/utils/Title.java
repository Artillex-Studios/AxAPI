package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface Title {

    static Title create(Component title) {
        return NMSHandlers.getNmsHandler().newTitle(title, null, 10, 70, 20);
    }

    static Title create(Component title, Component subTitle) {
        return NMSHandlers.getNmsHandler().newTitle(title, subTitle, 10, 70, 20);
    }

    static Title create(Component title, Component subTitle, int fadeIn, int stay, int fadeOut) {
        return NMSHandlers.getNmsHandler().newTitle(title, subTitle, fadeIn, stay, fadeOut);
    }

    static Title create(Component subTitle, int fadeIn, int stay, int fadeOut) {
        return NMSHandlers.getNmsHandler().newTitle(null, subTitle, fadeIn, stay, fadeOut);
    }

    static void send(Player player, Component title, Component subTitle) {
        Title titleObject = create(title, subTitle);
        titleObject.send(player);
    }

    static void sendTitle(Player player, Component title) {
        Title titleObject = create(title);
        titleObject.send(player);
    }

    static void sendSubTitle(Player player, Component subTitle) {
        Title titleObject = create(null, subTitle);
        titleObject.send(player);
    }

    void setTitle(@Nullable Component title);

    void setSubTitle(@Nullable Component subTitle);

    void set(@Nullable Component title, @Nullable Component subTitle);

    void set(@Nullable Component title, @Nullable Component subTitle, int fadeIn, int stay, int fadeOut);

    void setTimes(int fadeIn, int stay, int fadeOut);

    void setFadeInTime(int fadeIn);

    void setStayTime(int stayTime);

    void setFadeOutTime(int fateOut);

    void send(Player player);

    void clear(Player player);
}
