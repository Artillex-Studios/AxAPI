package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetActionBarTextWrapper;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public final class ActionBar {
    private Component title;
    private ClientboundSetActionBarTextWrapper wrapper;

    public ActionBar(Component title) {
        this.title = title;
        this.updatePacket();
    }

    public static ActionBar create(Component title) {
        return new ActionBar(title);
    }

    public static void send(Player player, Component title) {
        ActionBar actionBar = create(title);
        actionBar.send(player);
    }

    public void setContent(Component component) {
        this.title = component;
        this.updatePacket();
    }

    public void send(Player player) {
        Preconditions.checkArgument(this.wrapper != null);
        ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
        wrapper.sendPacket(this.wrapper);
    }

    private void updatePacket() {
        this.wrapper = new ClientboundSetActionBarTextWrapper(this.title);
    }
}
