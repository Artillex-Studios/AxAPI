package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundBossEventWrapper;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public final class BossBar {
    private final UUID uuid = UUID.randomUUID();
    private final Set<Flag> flags = EnumSet.noneOf(Flag.class);
    private final Set<Player> viewers = Collections.newSetFromMap(new WeakHashMap<>());
    private final ClientboundBossEventWrapper removePacket = new ClientboundBossEventWrapper(this.uuid, ClientboundBossEventWrapper.REMOVE_ACTION);
    private Component title;
    private Color color;
    private Style style;
    private float progress;
    private ClientboundBossEventWrapper addPacket;

    public BossBar(Component title, float progress, Color color, Style style, Flag... flags) {
        Validate.inclusiveBetween(0.0, 1.0, progress);
        this.title = title;
        this.progress = progress;
        this.color = color;
        this.style = style;
        this.flags.addAll(Arrays.asList(flags));
    }

    public static BossBar create(Component title) {
        return new BossBar(title, 1.0f, Color.PINK, Style.PROGRESS);
    }

    public static BossBar create(Component title, float progress) {
        return new BossBar(title, progress, Color.PINK, Style.PROGRESS);
    }

    public static BossBar create(Component title, float progress, Color color) {
        return new BossBar(title, progress, color, Style.PROGRESS);
    }

    public static BossBar create(Component title, float progress, Color color, Style style) {
        return new BossBar(title, progress, color, style);
    }

    public static BossBar create(Component title, float progress, Color color, Style style, Flag... flags) {
        return new BossBar(title, progress, color, style, flags);
    }

    public void show(Player player) {
        if (!this.viewers.add(player)) {
            return;
        }

        ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
        wrapper.sendPacket(this.addPacket);
    }

    public void hide(Player player) {
        if (this.viewers.remove(player)) {
            return;
        }

        ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
        wrapper.sendPacket(this.removePacket);
    }

    public void style(Style style) {
        this.style = style;
        this.updatePacket();
        this.broadcast(new ClientboundBossEventWrapper(this.uuid, new ClientboundBossEventWrapper.UpdateStyleAction(this.color, this.style)));
    }

    public void color(Color color) {
        this.color = color;
        this.updatePacket();
        this.broadcast(new ClientboundBossEventWrapper(this.uuid, new ClientboundBossEventWrapper.UpdateStyleAction(this.color, this.style)));
    }

    public void addFlags(Flag... flag) {
        this.flags.addAll(Arrays.asList(flag));
        this.updatePacket();
        this.broadcast(new ClientboundBossEventWrapper(this.uuid, new ClientboundBossEventWrapper.UpdateFlagsAction(this.flags)));
    }

    public void removeFlags(Flag... flag) {
        Arrays.asList(flag).forEach(this.flags::remove);
        this.updatePacket();
        this.broadcast(new ClientboundBossEventWrapper(this.uuid, new ClientboundBossEventWrapper.UpdateFlagsAction(this.flags)));
    }

    public Set<Flag> flags() {
        return this.flags;
    }

    public Component title() {
        return this.title;
    }

    public void title(Component title) {
        this.title = title;
        this.updatePacket();
        this.broadcast(new ClientboundBossEventWrapper(this.uuid, new ClientboundBossEventWrapper.UpdateNameAction(this.title)));
    }

    public float progress() {
        return this.progress;
    }

    public void progress(float progress) {
        this.progress = progress;
        this.updatePacket();
        this.broadcast(new ClientboundBossEventWrapper(this.uuid, new ClientboundBossEventWrapper.UpdateProgressAction(this.progress)));
    }

    public void remove() {
        this.broadcast(this.removePacket);
        this.viewers.clear();
    }

    private void updatePacket() {
        this.addPacket = new ClientboundBossEventWrapper(this.uuid, new ClientboundBossEventWrapper.AddAction(this.title, this.progress, this.color, this.style, this.flags));
    }

    private void broadcast(PacketWrapper packetWrapper) {
        for (Player viewer : this.viewers) {
            ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(viewer);
            wrapper.sendPacket(packetWrapper);
        }
    }

    public enum Style {
        PROGRESS(Arrays.asList("progress", "solid"), "progress"),
        NOTCHED_6(Arrays.asList("notched_6", "segmented_6"), "notched_6"),
        NOTCHED_10(Arrays.asList("notched_10", "segmented_10"), "notched_10"),
        NOTCHED_12(Arrays.asList("notched_12", "segmented_12"), "notched_12"),
        NOTCHED_20(Arrays.asList("notched_20", "segmented_20"), "notched_20");

        private static final HashMap<List<String>, Style> STYLES = new HashMap<>();

        static {
            for (Style value : values()) {
                STYLES.put(value.styleNames, value);
            }
        }

        private final List<String> styleNames;
        private final String internalName;

        Style(List<String> styleNames, String internalName) {
            this.styleNames = styleNames;
            this.internalName = internalName;
        }

        public static Style parse(String style) {
            for (Map.Entry<List<String>, Style> listStyleEntry : STYLES.entrySet()) {
                List<String> names = listStyleEntry.getKey();

                for (String name : names) {
                    if (name.equalsIgnoreCase(style)) {
                        return listStyleEntry.getValue();
                    }
                }
            }

            return null;
        }

        public String getInternalName() {
            return internalName;
        }
    }

    public enum Color {
        PINK("pink"),
        BLUE("blue"),
        RED("red"),
        GREEN("green"),
        YELLOW("yellow"),
        PURPLE("purple"),
        WHITE("white");

        private final String internalName;

        Color(String internalName) {
            this.internalName = internalName;
        }

        public static Color parse(String color) {
            return Color.valueOf(color.toUpperCase(Locale.ENGLISH));
        }

        public String getInternalName() {
            return internalName;
        }
    }

    public enum Flag {
        DARKEN_SCREEN("darken_screen"),
        PLAY_BOSS_MUSIC("play_boss_music"),
        CREATE_WORLD_FOG("create_world_fog");

        private final String internalName;

        Flag(final String internalName) {
            this.internalName = internalName;
        }

        public static Flag parse(String flag) {
            return Flag.valueOf(flag.toUpperCase(Locale.ENGLISH));
        }

        public String getInternalName() {
            return internalName;
        }
    }
}
