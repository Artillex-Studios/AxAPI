package com.artillexstudios.axapi.nms.v1_20_R4.utils;

import com.artillexstudios.axapi.utils.ComponentSerializer;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.entity.CraftPlayer;
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
    private BossEvent event;

    public BossBar(Component title, float progress, Color color, Style style, Flag... flags) {
        Validate.inclusiveBetween(0.0, 1.0, progress);
        this.title = title;
        this.progress = progress;
        this.color = color;
        this.style = style;
        this.flags.addAll(Arrays.asList(flags));

        event = new BossEvent(uuid, ComponentSerializer.INSTANCE.toVanilla(title), BossEvent.BossBarColor.byName(color.getInternalName()), BossEvent.BossBarOverlay.byName(style.getInternalName())) {};
        updateAddPacket();
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
        event.setOverlay(BossEvent.BossBarOverlay.byName(this.style.getInternalName()));
        updateAddPacket();
        updateStyle();
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        event.setColor(BossEvent.BossBarColor.byName(this.color.getInternalName()));
        updateAddPacket();
        updateStyle();
    }

    @Override
    public void addFlags(Flag... flag) {
        flags.addAll(Arrays.asList(flag));
        updateAddPacket();
        updateFlags();

        for (Flag flag1 : flag) {
            if (flag1 == Flag.PLAY_BOSS_MUSIC) {
                event.setPlayBossMusic(true);
            } else if (flag1 == Flag.CREATE_WORLD_FOG) {
                event.setCreateWorldFog(true);
            } else if (flag1 == Flag.DARKEN_SCREEN) {
                event.setDarkenScreen(true);
            }
        }
    }

    @Override
    public void removeFlags(Flag... flag) {
        Arrays.asList(flag).forEach(flags::remove);

        for (Flag flag1 : flag) {
            if (flag1 == Flag.PLAY_BOSS_MUSIC) {
                event.setPlayBossMusic(false);
            } else if (flag1 == Flag.CREATE_WORLD_FOG) {
                event.setCreateWorldFog(false);
            } else if (flag1 == Flag.DARKEN_SCREEN) {
                event.setDarkenScreen(false);
            }
        }
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
        event.setName(ComponentSerializer.INSTANCE.toVanilla(title));
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
        event.setProgress(progress);
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
        ClientboundBossEventPacket titlePacket = ClientboundBossEventPacket.createUpdateNamePacket(event);

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(titlePacket);
        }
    }

    private void updateProgress() {
        ClientboundBossEventPacket progressPacket = ClientboundBossEventPacket.createUpdateProgressPacket(event);

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(progressPacket);
        }
    }

    private void updateStyle() {
        ClientboundBossEventPacket progressPacket = ClientboundBossEventPacket.createUpdateStylePacket(event);

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(progressPacket);
        }
    }

    private void updateFlags() {
        ClientboundBossEventPacket progressPacket = ClientboundBossEventPacket.createUpdatePropertiesPacket(event);

        for (Player viewer : viewers) {
            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            serverPlayer.connection.send(progressPacket);
        }
    }

    private void updateAddPacket() {
        addPacket = ClientboundBossEventPacket.createAddPacket(event);
    }
}
