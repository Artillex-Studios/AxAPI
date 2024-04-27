package com.artillexstudios.axapi.items.component;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class ItemLore {
    private static final Style DEFAULT_STYLE = Style.empty().color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC);
    private final List<Component> lines;
    private final List<Component> styledLines;

    public ItemLore(List<Component> lines) {
        this(lines, Lists.transform(lines, line -> mergeStyles(line, DEFAULT_STYLE)));
    }

    public ItemLore(List<Component> lines, List<Component> styledLines) {
        if (lines.size() > 256) {
            throw new IllegalArgumentException("Got " + lines.size() + " lines, but maximum is 256");
        }

        this.lines = lines;
        this.styledLines = styledLines;
    }

    public List<Component> styledLines() {
        return styledLines;
    }

    public List<Component> lines() {
        return lines;
    }

    // Code copied from vanilla
    private static Component mergeStyles(Component text, Style style) {
        if (style.isEmpty()) {
            return text;
        } else {
            Style style2 = text.style();
            if (style2.isEmpty()) {
                return text.style(style);
            } else {
                return style2.equals(style) ? text : text.style(style2.merge(style));
            }
        }
    }
}
