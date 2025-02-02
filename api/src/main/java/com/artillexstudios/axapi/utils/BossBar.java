package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface BossBar {

    static BossBar create(Component title) {
        return NMSHandlers.getNmsHandler().newBossBar(title, 1.0f, Color.PINK, Style.PROGRESS);
    }

    static BossBar create(Component title, float progress) {
        return NMSHandlers.getNmsHandler().newBossBar(title, progress, Color.PINK, Style.PROGRESS);
    }

    static BossBar create(Component title, float progress, Color color) {
        return NMSHandlers.getNmsHandler().newBossBar(title, progress, color, Style.PROGRESS);
    }

    static BossBar create(Component title, float progress, Color color, Style style) {
        return NMSHandlers.getNmsHandler().newBossBar(title, progress, color, style);
    }

    static BossBar create(Component title, float progress, Color color, Style style, Flag... flags) {
        return NMSHandlers.getNmsHandler().newBossBar(title, progress, color, style, flags);
    }

    void show(Player player);

    void hide(Player player);

    void setStyle(Style style);

    void setColor(Color color);

    void addFlags(Flag... flag);

    void removeFlags(Flag... flag);

    Set<Flag> getFlags();

    Component getTitle();

    void setTitle(Component title);

    float getProgress();

    void setProgress(float progress);

    void remove();

    enum Style {
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

    enum Color {
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
            return Color.valueOf(color.toLowerCase(Locale.ENGLISH));
        }

        public String getInternalName() {
            return internalName;
        }
    }

    enum Flag {
        DARKEN_SCREEN("darken_screen"),
        PLAY_BOSS_MUSIC("play_boss_music"),
        CREATE_WORLD_FOG("create_world_fog");

        private final String internalName;

        Flag(final String internalName) {
            this.internalName = internalName;
        }

        public static Flag parse(String flag) {
            return Flag.valueOf(flag.toLowerCase(Locale.ENGLISH));
        }

        public String getInternalName() {
            return internalName;
        }
    }
}
